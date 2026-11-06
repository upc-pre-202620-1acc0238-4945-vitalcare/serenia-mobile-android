package com.vitalcare.serenia.features.socialcompanionship.domain

data class PhotoMessage(
    val id: String,
    val senderId: String,
    val mediaUrl: String,
    val sentAtMillis: Long,
    val isViewed: Boolean
)
