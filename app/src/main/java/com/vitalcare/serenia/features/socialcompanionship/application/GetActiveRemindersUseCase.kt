package com.vitalcare.serenia.features.socialcompanionship.application

import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminder
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminderRepository
import javax.inject.Inject

class GetActiveRemindersUseCase @Inject constructor(private val repository: SocialReminderRepository) {

    suspend operator fun invoke(): Result<List<SocialReminder>> {
        return repository.getActiveReminders()
    }
}
