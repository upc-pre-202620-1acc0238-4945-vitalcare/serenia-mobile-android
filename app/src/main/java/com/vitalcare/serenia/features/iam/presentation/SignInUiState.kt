package com.vitalcare.serenia.features.iam.presentation

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val formError: String? = null,
    val credentialsRejected: Boolean = false,
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false
)
