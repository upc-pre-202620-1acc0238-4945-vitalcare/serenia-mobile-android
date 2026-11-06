package com.vitalcare.serenia.features.socialcompanionship.infrastructure

data class ScheduleReminderRequestDto(
    val title: String,
    val description: String?,
    val remindAt: String
)
