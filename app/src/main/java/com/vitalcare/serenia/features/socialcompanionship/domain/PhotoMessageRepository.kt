package com.vitalcare.serenia.features.socialcompanionship.domain

interface PhotoMessageRepository {

    suspend fun getPhotoMessages(): Result<List<PhotoMessage>>

    suspend fun viewPhotoMessage(photoMessageId: String): Result<PhotoMessage>
}
