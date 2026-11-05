package com.vitalcare.serenia.features.iam.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.vitalcare.serenia.core.network.ApiException
import com.vitalcare.serenia.features.iam.application.SignUpUseCase
import com.vitalcare.serenia.features.iam.domain.CredentialsValidator
import com.vitalcare.serenia.features.iam.domain.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val signUp: SignUpUseCase
) : ViewModel() {

    private val role = UserRole.valueOf(savedStateHandle.toRoute<SignUpRoute>().role)

    private val _state = MutableStateFlow(SignUpUiState())
    val state: StateFlow<SignUpUiState> = _state.asStateFlow()

    fun onFullNameChange(fullName: String) {
        _state.update { it.copy(fullName = fullName, fullNameError = null) }
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, passwordError = null) }
    }

    fun submit() {
        val currentState = _state.value
        if (currentState.isLoading) return

        val fullNameError = when {
            currentState.fullName.isBlank() -> "Escribe tu nombre."
            !CredentialsValidator.isValidName(currentState.fullName) -> "Tu nombre es muy largo. Usa uno más corto."
            else -> null
        }
        val emailError = when {
            currentState.email.isBlank() -> "Escribe tu correo."
            !CredentialsValidator.isValidEmail(currentState.email) -> "Revisa el correo: parece incompleto."
            else -> null
        }
        val passwordError = when {
            currentState.password.length < CredentialsValidator.MIN_PASSWORD_LENGTH ->
                "Debe tener al menos 8 letras o números."
            !CredentialsValidator.isValidPassword(currentState.password) ->
                "Debe tener como máximo 72 letras o números."
            else -> null
        }

        if (fullNameError != null || emailError != null || passwordError != null) {
            _state.update {
                it.copy(
                    fullNameError = fullNameError,
                    emailError = emailError,
                    passwordError = passwordError,
                    formError = null
                )
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, formError = null) }

            signUp(currentState.fullName, currentState.email, currentState.password, role).fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isSignedUp = true) }
                },
                onFailure = { exception ->
                    if (exception is ApiException && exception.status == CONFLICT) {
                        _state.update { it.copy(isLoading = false, emailError = EMAIL_TAKEN) }
                    } else {
                        _state.update { it.copy(isLoading = false, formError = exception.toAuthMessage()) }
                    }
                }
            )
        }
    }

    private companion object {
        const val CONFLICT = 409
        const val EMAIL_TAKEN = "Este correo ya tiene una cuenta. Inicia sesión o usa otro correo."
    }
}
