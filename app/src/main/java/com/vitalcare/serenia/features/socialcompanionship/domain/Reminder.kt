package com.vitalcare.serenia.features.socialcompanionship.domain

data class Reminder(
    val id: Int,
    val title: String,
    val schedule: String,
    val status: ReminderStatus,
    val isDue: Boolean
)
