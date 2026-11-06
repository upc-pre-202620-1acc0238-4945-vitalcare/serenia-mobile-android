package com.vitalcare.serenia.features.iam.presentation

import com.vitalcare.serenia.features.iam.domain.BirthDate

data class SignUpUiState(
    val fullName: String = "",
    val birthDate: BirthDate? = null,
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val fullNameError: String? = null,
    val phoneNumberError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val formError: String? = null,
    val isLoading: Boolean = false,
    val isSignedUp: Boolean = false
)
