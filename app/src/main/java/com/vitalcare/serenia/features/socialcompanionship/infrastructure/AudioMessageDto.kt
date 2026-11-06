package com.vitalcare.serenia.features.socialcompanionship.infrastructure

data class AudioMessageDto(
    val id: String,
    val careCircleId: String,
    val senderId: String,
    val status: String,
    val durationSeconds: Int,
    val mediaUrl: String,
    val createdAt: String,
    val sentAt: String?,
    val played: Boolean
)
