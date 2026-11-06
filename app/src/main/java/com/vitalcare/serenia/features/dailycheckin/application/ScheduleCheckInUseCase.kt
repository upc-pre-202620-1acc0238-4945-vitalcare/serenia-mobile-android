package com.vitalcare.serenia.features.dailycheckin.application

import com.vitalcare.serenia.features.dailycheckin.domain.CheckInPreferences
import com.vitalcare.serenia.features.dailycheckin.domain.CheckInPreferencesRepository
import com.vitalcare.serenia.features.dailycheckin.domain.ReminderTime
import com.vitalcare.serenia.features.dailycheckin.domain.ReminderTimeOutOfRangeException
import javax.inject.Inject

class ScheduleCheckInUseCase @Inject constructor(private val repository: CheckInPreferencesRepository) {

    suspend operator fun invoke(reminderTime: ReminderTime): Result<CheckInPreferences> {
        if (!reminderTime.isWithinAllowedRange) {
            return Result.failure(ReminderTimeOutOfRangeException())
        }
        return repository.scheduleCheckIn(reminderTime)
    }
}
