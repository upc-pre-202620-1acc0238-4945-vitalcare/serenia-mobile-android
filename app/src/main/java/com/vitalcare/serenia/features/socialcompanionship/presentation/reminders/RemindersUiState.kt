package com.vitalcare.serenia.features.socialcompanionship.presentation.reminders

import com.vitalcare.serenia.features.socialcompanionship.domain.Reminder

data class RemindersUiState(
    val reminders: List<Reminder> = emptyList()
)

// One-time events that the screen shows once and then forgets
sealed interface RemindersEvent {
    data object ReminderCancelled : RemindersEvent
    data object ReminderSaved : RemindersEvent
}
