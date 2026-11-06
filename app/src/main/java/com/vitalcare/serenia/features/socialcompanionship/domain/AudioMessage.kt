package com.vitalcare.serenia.features.socialcompanionship.domain

data class AudioMessage(
    val id: String,
    val durationSeconds: Int,
    val isShared: Boolean
)
