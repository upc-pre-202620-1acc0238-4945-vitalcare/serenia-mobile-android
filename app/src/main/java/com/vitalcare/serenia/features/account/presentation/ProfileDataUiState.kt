package com.vitalcare.serenia.features.account.presentation

import com.vitalcare.serenia.features.iam.domain.BirthDate

data class ProfileDataUiState(
    val isLoading: Boolean = true,
    val fullName: String = "",
    val birthDate: BirthDate? = null,
    val phoneNumber: String = "",
    val fullNameError: String? = null,
    val phoneNumberError: String? = null,
    val formError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
) {
    val initial: String
        get() = fullName.trim().firstOrNull()?.uppercase().orEmpty()
}
