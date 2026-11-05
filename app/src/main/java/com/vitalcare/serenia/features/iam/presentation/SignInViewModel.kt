package com.vitalcare.serenia.features.iam.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.network.ApiException
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.iam.application.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val signIn: SignInUseCase) : ViewModel() {

    private val _state = MutableStateFlow(SignInUiState())
    val state: StateFlow<SignInUiState> = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update { currentState ->
            currentState.copy(email = email, emailError = null, credentialsRejected = false)
        }
    }

    fun onPasswordChange(password: String) {
        _state.update { currentState ->
            currentState.copy(password = password, passwordError = null, credentialsRejected = false)
        }
    }

    fun submit() {
        val currentState = _state.value
        if (currentState.isLoading) return

        val emailError = if (currentState.email.isBlank()) "Escribe tu correo." else null
        val passwordError = if (currentState.password.isBlank()) "Escribe tu contraseña." else null

        if (emailError != null || passwordError != null) {
            _state.update { it.copy(emailError = emailError, passwordError = passwordError, formError = null) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, formError = null, credentialsRejected = false) }

            signIn(currentState.email, currentState.password).fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isSignedIn = true) }
                },
                onFailure = { exception ->
                    if (exception is ApiException && exception.status == UNAUTHORIZED) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                password = "",
                                credentialsRejected = true,
                                formError = WRONG_CREDENTIALS
                            )
                        }
                    } else {
                        _state.update { it.copy(isLoading = false, formError = exception.toUserMessage()) }
                    }
                }
            )
        }
    }

    private companion object {
        const val UNAUTHORIZED = 401
        const val WRONG_CREDENTIALS = "El correo o la contraseña no son correctos. Revisa e intenta de nuevo."
    }
}
