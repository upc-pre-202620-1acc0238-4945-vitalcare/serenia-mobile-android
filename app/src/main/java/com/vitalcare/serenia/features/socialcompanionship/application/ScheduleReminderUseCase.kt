package com.vitalcare.serenia.features.socialcompanionship.application

import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderDateTime
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderTimeInPastException
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderTitleRequiredException
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminder
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminderRepository
import javax.inject.Inject

class ScheduleReminderUseCase @Inject constructor(private val repository: SocialReminderRepository) {

    suspend operator fun invoke(title: String, remindAt: ReminderDateTime): Result<SocialReminder> {
        if (title.isBlank()) {
            return Result.failure(ReminderTitleRequiredException())
        }
        if (remindAt <= ReminderDateTime.now()) {
            return Result.failure(ReminderTimeInPastException())
        }
        return repository.scheduleReminder(title.trim(), remindAt)
    }
}
