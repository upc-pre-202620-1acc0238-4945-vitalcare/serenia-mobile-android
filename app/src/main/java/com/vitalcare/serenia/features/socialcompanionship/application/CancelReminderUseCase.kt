package com.vitalcare.serenia.features.socialcompanionship.application

import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderRepository
import javax.inject.Inject

class CancelReminderUseCase @Inject constructor(private val repository: ReminderRepository) {

    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.cancelReminder(id)
    }
}
