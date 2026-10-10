package com.vitalcare.serenia.features.socialcompanionship.application

import kotlinx.coroutines.flow.Flow
import com.vitalcare.serenia.features.socialcompanionship.domain.Reminder
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderRepository
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(private val repository: ReminderRepository) {

    operator fun invoke(): Flow<List<Reminder>> {
        return repository.getReminders()
    }
}
