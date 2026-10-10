package com.vitalcare.serenia.features.socialcompanionship.domain

import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    fun getReminders(): Flow<List<Reminder>>

    suspend fun completeReminder(id: Int): Result<Unit>

    suspend fun postponeReminder(id: Int): Result<Unit>

    suspend fun cancelReminder(id: Int): Result<Unit>

    suspend fun createReminder(type: ReminderType, schedule: ReminderSchedule): Result<Reminder>
}
