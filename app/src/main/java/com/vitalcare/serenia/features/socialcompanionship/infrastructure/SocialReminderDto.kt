package com.vitalcare.serenia.features.socialcompanionship.infrastructure

data class SocialReminderDto(
    val id: String,
    val olderAdultId: String,
    val title: String,
    val description: String?,
    val remindAt: String,
    val status: String,
    val completedAt: String?
)
