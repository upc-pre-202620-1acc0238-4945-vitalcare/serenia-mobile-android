package com.vitalcare.serenia.features.iam.presentation

data class SignUpUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val formError: String? = null,
    val isLoading: Boolean = false,
    val isSignedUp: Boolean = false
)
