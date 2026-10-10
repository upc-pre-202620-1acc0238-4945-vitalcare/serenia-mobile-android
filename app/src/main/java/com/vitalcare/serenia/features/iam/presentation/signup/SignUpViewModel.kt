package com.vitalcare.serenia.features.iam.presentation.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.vitalcare.serenia.features.iam.domain.CredentialsValidator
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, nameError = false) }
    }

    fun onIdentifierChange(identifier: String) {
        _uiState.update { it.copy(identifier = identifier, identifierError = false) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = false) }
    }

    /**
     * Validates the form and marks the fields that need attention.
     * Returns true when the account can be created.
     */
    fun submit(): Boolean {
        val current = _uiState.value

        val updated = current.copy(
            nameError = !CredentialsValidator.isValidName(current.name),
            identifierError = !CredentialsValidator.isValidIdentifier(current.identifier),
            passwordError = !CredentialsValidator.isValidPassword(current.password)
        )
        _uiState.value = updated

        // The account will be created through the IAM API once it is connected
        return !updated.hasErrors
    }
}
