package com.vitalcare.serenia.features.account.presentation

import com.vitalcare.serenia.features.dailycheckin.domain.ReminderTime

data class AccountUiState(
    val fullName: String = "",
    val reminderTime: ReminderTime? = null,
    val chosenTime: ReminderTime? = null,
    val isPickerVisible: Boolean = false,
    val isSavingTime: Boolean = false,
    val timeError: String? = null,
    val simplifiedMode: Boolean = false,
    val isSavingSimplifiedMode: Boolean = false,
    val appearanceError: String? = null,
    val isConfirmingSignOut: Boolean = false,
    val isSigningOut: Boolean = false,
    val signOutError: String? = null
) {
    val initial: String
        get() = fullName.trim().firstOrNull()?.uppercase().orEmpty()

    val displayedTime: ReminderTime?
        get() = chosenTime ?: reminderTime
}
