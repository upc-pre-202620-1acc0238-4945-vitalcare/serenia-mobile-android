package com.vitalcare.serenia.features.socialcompanionship.application

import com.vitalcare.serenia.features.socialcompanionship.domain.PhotoMessage
import com.vitalcare.serenia.features.socialcompanionship.domain.PhotoMessageRepository
import javax.inject.Inject

class GetPhotoMessagesUseCase @Inject constructor(private val repository: PhotoMessageRepository) {

    suspend operator fun invoke(): Result<List<PhotoMessage>> {
        return repository.getPhotoMessages()
    }
}
