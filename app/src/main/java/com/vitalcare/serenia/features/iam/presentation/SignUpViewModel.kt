package com.vitalcare.serenia.features.iam.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.vitalcare.serenia.core.network.ApiException
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.iam.application.SignUpUseCase
import com.vitalcare.serenia.features.iam.domain.BirthDate
import com.vitalcare.serenia.features.iam.domain.CredentialsValidator
import com.vitalcare.serenia.features.iam.domain.PhoneNumber
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

    fun onBirthDateChange(birthDate: BirthDate) {
        _state.update { it.copy(birthDate = birthDate) }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _state.update { it.copy(phoneNumber = phoneNumber.toPhoneDigits(), phoneNumberError = null) }
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

        val fullNameError = fullNameErrorFor(currentState.fullName)
        val phoneNumberError = phoneNumberErrorFor(currentState.phoneNumber)
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

        if (fullNameError != null || phoneNumberError != null || emailError != null || passwordError != null) {
            _state.update {
                it.copy(
                    fullNameError = fullNameError,
                    phoneNumberError = phoneNumberError,
                    emailError = emailError,
                    passwordError = passwordError,
                    formError = null
                )
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, formError = null) }

            val phoneNumber = currentState.phoneNumber.ifEmpty { null }?.let(PhoneNumber::fromLocal)

            signUp(
                fullName = currentState.fullName,
                email = currentState.email,
                password = currentState.password,
                role = role,
                phoneNumber = phoneNumber,
                birthDate = currentState.birthDate
            ).fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isSignedUp = true) }
                },
                onFailure = { exception ->
                    if (exception is ApiException && exception.status == CONFLICT) {
                        _state.update { it.copy(isLoading = false, emailError = EMAIL_TAKEN) }
                    } else {
                        _state.update { it.copy(isLoading = false, formError = exception.toUserMessage()) }
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
