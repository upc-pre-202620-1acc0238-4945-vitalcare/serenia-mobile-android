package com.vitalcare.serenia.features.dailycheckin.domain

import kotlinx.coroutines.flow.StateFlow

interface CheckInPreferencesRepository {

    val preferences: StateFlow<CheckInPreferences?>

    suspend fun getPreferences(): Result<CheckInPreferences>

    suspend fun scheduleCheckIn(reminderTime: ReminderTime): Result<CheckInPreferences>

    suspend fun activateDailyPause(): Result<CheckInPreferences>

    suspend fun resumeDailyCheckIn(): Result<CheckInPreferences>

    suspend fun updateSimplifiedMode(enabled: Boolean): Result<CheckInPreferences>

    fun clear()
}
