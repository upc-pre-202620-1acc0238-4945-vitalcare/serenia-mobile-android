package com.vitalcare.serenia.features.socialcompanionship.presentation.newreminder

import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderSchedule
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderType

data class NewReminderUiState(
    val selectedType: ReminderType = ReminderType.CALL_FRIEND,
    val selectedSchedule: ReminderSchedule = ReminderSchedule.TODAY_EVENING,
    val isSaving: Boolean = false
)

// One-time events that the screen handles once and then forgets
sealed interface NewReminderEvent {
    data object ReminderSaved : NewReminderEvent
}
