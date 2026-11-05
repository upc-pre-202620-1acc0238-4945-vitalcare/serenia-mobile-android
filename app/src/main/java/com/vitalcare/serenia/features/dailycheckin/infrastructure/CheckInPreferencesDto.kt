package com.vitalcare.serenia.features.dailycheckin.infrastructure

data class CheckInPreferencesDto(
    val id: String,
    val olderAdultId: String,
    val reminderTime: String,
    val timeLimitMinutes: Int,
    val simplifiedMode: Boolean,
    val pausedToday: Boolean
)
