package com.vitalcare.serenia.features.iam.presentation.signin

data class SignInUiState(
    val identifier: String = "",
    val password: String = "",
    val identifierError: Boolean = false,
    val passwordError: Boolean = false
) {
    val hasErrors: Boolean
        get() = identifierError || passwordError
}
