package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import com.vitalcare.serenia.core.network.toApiException
import com.vitalcare.serenia.features.carecircle.application.GetCareCircleIdUseCase
import com.vitalcare.serenia.features.socialcompanionship.domain.PhotoMessage
import com.vitalcare.serenia.features.socialcompanionship.domain.PhotoMessageRepository
import retrofit2.Response
import javax.inject.Inject

class PhotoMessageRepositoryImpl @Inject constructor(
    private val api: SocialCompanionshipApi,
    private val getCareCircleId: GetCareCircleIdUseCase
) : PhotoMessageRepository {

    override suspend fun getPhotoMessages(): Result<List<PhotoMessage>> {
        return request { careCircleId ->
            api.getPhotoMessages(careCircleId).toResult { photos ->
                photos.map { it.toDomain() }.sortedByDescending { it.sentAtMillis }
            }
        }
    }

    override suspend fun viewPhotoMessage(photoMessageId: String): Result<PhotoMessage> {
        return request { careCircleId ->
            api.viewPhotoMessage(careCircleId, photoMessageId).toResult { it.toDomain() }
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
}
