package com.vitalcare.serenia.features.socialcompanionship.infrastructure

data class PhotoMessageDto(
    val id: String,
    val careCircleId: String,
    val senderId: String,
    val status: String,
    val mediaUrl: String,
    val createdAt: String,
    val sentAt: String?,
    val viewed: Boolean
)
