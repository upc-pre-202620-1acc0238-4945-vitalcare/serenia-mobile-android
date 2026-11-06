package com.vitalcare.serenia.features.socialcompanionship.domain

interface SocialReminderRepository {

    suspend fun getActiveReminders(): Result<List<SocialReminder>>

    suspend fun scheduleReminder(title: String, remindAt: ReminderDateTime): Result<SocialReminder>

    suspend fun postponeReminder(reminderId: String): Result<SocialReminder>

    suspend fun completeReminder(reminderId: String): Result<SocialReminder>

    suspend fun cancelReminder(reminderId: String): Result<SocialReminder>
}
