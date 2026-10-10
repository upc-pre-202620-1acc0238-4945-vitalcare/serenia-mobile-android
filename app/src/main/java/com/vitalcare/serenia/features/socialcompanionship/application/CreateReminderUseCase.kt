package com.vitalcare.serenia.features.socialcompanionship.application

import com.vitalcare.serenia.features.socialcompanionship.domain.Reminder
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderRepository
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderSchedule
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderType
import javax.inject.Inject

class CreateReminderUseCase @Inject constructor(private val repository: ReminderRepository) {

    suspend operator fun invoke(type: ReminderType, schedule: ReminderSchedule): Result<Reminder> {
        return repository.createReminder(type, schedule)
    }
}
