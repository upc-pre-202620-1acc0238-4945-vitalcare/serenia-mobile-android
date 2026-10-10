package com.vitalcare.serenia.features.iam.presentation.signup

data class SignUpUiState(
    val name: String = "",
    val identifier: String = "",
    val password: String = "",
    val nameError: Boolean = false,
    val identifierError: Boolean = false,
    val passwordError: Boolean = false
) {
    val hasErrors: Boolean
        get() = nameError || identifierError || passwordError
}
