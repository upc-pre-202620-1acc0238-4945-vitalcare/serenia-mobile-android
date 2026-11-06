package com.vitalcare.serenia.features.socialcompanionship.presentation

import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderDateTime
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminder

data class RemindersUiState(
    val reminders: List<SocialReminder>? = null,
    val loadError: String? = null,
    val completedReminder: SocialReminder? = null,
    val reminderToCancel: String? = null,
    val busyReminderId: String? = null,
    val actionErrorReminderId: String? = null,
    val actionError: String? = null,
    val message: String? = null
) {
    val nextReminder: SocialReminder?
        get() = dueReminders.firstOrNull() ?: reminders.orEmpty().minByOrNull { it.remindAt }

    val dueReminders: List<SocialReminder>
        get() = reminders.orEmpty().filter { it.isDue }

    val todayReminders: List<SocialReminder>
        get() {
            val now = ReminderDateTime.now()
            return reminders.orEmpty().filter { !it.isDue && it.remindAt.isSameDayAs(now) }
        }

    val upcomingReminders: List<SocialReminder>
        get() {
            val now = ReminderDateTime.now()
            return reminders.orEmpty().filter { !it.isDue && !it.remindAt.isSameDayAs(now) }
        }
}
