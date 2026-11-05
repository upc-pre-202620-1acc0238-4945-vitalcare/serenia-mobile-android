package com.vitalcare.serenia.features.dailycheckin.infrastructure

data class CheckInDto(
    val id: String,
    val olderAdultId: String,
    val checkDate: String,
    val questionText: String?,
    val scheduledFor: String,
    val deadlineAt: String,
    val status: String,
    val paused: Boolean,
    val mood: String?,
    val positiveActivity: String?,
    val answeredAt: String?
)
