package com.vitalcare.serenia.features.socialcompanionship.application

import com.vitalcare.serenia.features.socialcompanionship.domain.AudioMessage
import com.vitalcare.serenia.features.socialcompanionship.domain.AudioMessageRepository
import java.io.File
import javax.inject.Inject

class SendAudioMessageUseCase @Inject constructor(private val repository: AudioMessageRepository) {

    suspend operator fun invoke(file: File, durationSeconds: Int): Result<AudioMessage> {
        val draft = repository.recordAudioMessage(file, durationSeconds.coerceIn(MIN_SECONDS, MAX_SECONDS))
            .getOrElse { return Result.failure(it) }

        return repository.shareAudioMessage(draft.id).onFailure {
            repository.discardAudioMessage(draft.id)
        }
    }

    private companion object {
        const val MIN_SECONDS = 1
        const val MAX_SECONDS = 180
    }
}
