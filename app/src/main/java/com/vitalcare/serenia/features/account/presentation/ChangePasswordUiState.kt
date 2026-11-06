package com.vitalcare.serenia.features.account.presentation

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val formError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)
