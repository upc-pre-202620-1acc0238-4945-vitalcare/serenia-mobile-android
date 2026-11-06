package com.vitalcare.serenia.features.dailycheckin.presentation

import com.vitalcare.serenia.features.dailycheckin.domain.ReminderTime

data class CheckInTimeUiState(
    val reminderTime: ReminderTime = ReminderTime.DEFAULT,
    val savedTime: ReminderTime? = null,
    val isPickerVisible: Boolean = false,
    val timeError: String? = null,
    val formError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)
