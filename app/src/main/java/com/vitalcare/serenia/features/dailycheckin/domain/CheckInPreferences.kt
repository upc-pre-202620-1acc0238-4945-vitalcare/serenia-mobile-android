package com.vitalcare.serenia.features.dailycheckin.domain

data class CheckInPreferences(
    val id: String,
    val reminderTime: ReminderTime,
    val simplifiedMode: Boolean,
    val pausedToday: Boolean
)
