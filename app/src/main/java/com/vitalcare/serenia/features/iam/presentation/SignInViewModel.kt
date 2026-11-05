package com.vitalcare.serenia.features.iam.presentation.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.vitalcare.serenia.features.iam.domain.CredentialsValidator
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun onIdentifierChange(identifier: String) {
        _uiState.update { it.copy(identifier = identifier, identifierError = false) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = false) }
    }

    /**
     * Validates the form and marks the fields that need attention.
     * Returns true when the user can be signed in.
     */
    fun submit(): Boolean {
        val current = _uiState.value

        val updated = current.copy(
            identifierError = !CredentialsValidator.isValidIdentifier(current.identifier),
            // On sign in only an empty password is rejected; the server checks the rest
            passwordError = current.password.isBlank()
        )
        _uiState.value = updated

        // The session will be created through the IAM API once it is connected
        return !updated.hasErrors
    }
}
