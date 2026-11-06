package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import com.vitalcare.serenia.core.network.toApiException
import com.vitalcare.serenia.features.carecircle.application.GetCareCircleIdUseCase
import com.vitalcare.serenia.features.socialcompanionship.domain.AudioMessage
import com.vitalcare.serenia.features.socialcompanionship.domain.AudioMessageRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class AudioMessageRepositoryImpl @Inject constructor(
    private val api: SocialCompanionshipApi,
    private val getCareCircleId: GetCareCircleIdUseCase
) : AudioMessageRepository {

    override suspend fun recordAudioMessage(file: File, durationSeconds: Int): Result<AudioMessage> {
        return request { careCircleId ->
            val filePart = MultipartBody.Part.createFormData(
                FILE_PART,
                file.name,
                file.asRequestBody(AUDIO_TYPE.toMediaType())
            )
            val duration = durationSeconds.toString().toRequestBody(TEXT_TYPE.toMediaType())
            api.recordAudioMessage(careCircleId, filePart, duration).toResult { it.toDomain() }
        }
    }

    override suspend fun shareAudioMessage(audioMessageId: String): Result<AudioMessage> {
        return request { careCircleId ->
            api.shareAudioMessage(careCircleId, audioMessageId).toResult { it.toDomain() }
        }
    }

    override suspend fun discardAudioMessage(audioMessageId: String): Result<Unit> {
        return request { careCircleId ->
            val response = api.discardAudioMessage(careCircleId, audioMessageId)
            if (response.isSuccessful) Result.success(Unit) else Result.failure(response.toApiException())
        }
    }

    private suspend fun <T> request(block: suspend (careCircleId: String) -> Result<T>): Result<T> {
        try {
            val careCircleId = getCareCircleId().getOrElse { return Result.failure(it) }
            return block(careCircleId)

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    private fun <D, T> Response<D>.toResult(transform: (D) -> T): Result<T> {
        if (isSuccessful) {
            body()?.let { dto ->
                return Result.success(transform(dto))
            }
        }
        return Result.failure(toApiException())
    }

    private companion object {
        const val FILE_PART = "file"
        const val AUDIO_TYPE = "audio/mp4"
        const val TEXT_TYPE = "text/plain"
    }
}
