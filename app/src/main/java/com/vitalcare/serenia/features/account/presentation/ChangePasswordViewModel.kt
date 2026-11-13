package com.vitalcare.serenia.features.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.iam.application.ChangePasswordUseCase
import com.vitalcare.serenia.features.iam.domain.CredentialsValidator
import com.vitalcare.serenia.features.iam.domain.IncorrectPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePassword: ChangePasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ChangePasswordUiState())
    val state: StateFlow<ChangePasswordUiState> = _state.asStateFlow()

    fun onCurrentPasswordChange(password: String) {
        _state.update { it.copy(currentPassword = password, currentPasswordError = null) }
    }

    fun onNewPasswordChange(password: String) {
        _state.update { it.copy(newPassword = password, newPasswordError = null) }
    }

    fun save() {
        val currentState = _state.value
        if (currentState.isSaving) return

        val currentPasswordError = if (currentState.currentPassword.isEmpty()) "Escribe tu contraseña actual." else null
        val newPasswordError = when {
            currentState.newPassword.length < CredentialsValidator.MIN_PASSWORD_LENGTH ->
                "Debe tener al menos 8 letras o números."
            !CredentialsValidator.isValidPassword(currentState.newPassword) ->
                "Debe tener como máximo 72 letras o números."
            else -> null
        }

        if (currentPasswordError != null || newPasswordError != null) {
            _state.update {
                it.copy(
                    currentPasswordError = currentPasswordError,
                    newPasswordError = newPasswordError,
                    formError = null
                )
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isSaving = true, formError = null) }

            changePassword(currentState.currentPassword, currentState.newPassword).fold(
                onSuccess = {
                    _state.update { it.copy(isSaving = false, isSaved = true) }
                },
                onFailure = { exception ->
                    if (exception is IncorrectPasswordException) {
                        _state.update { it.copy(isSaving = false, currentPasswordError = INCORRECT_PASSWORD) }
                    } else {
                        _state.update { it.copy(isSaving = false, formError = exception.toUserMessage()) }
                    }
                }
            )
        }
    }

    private companion object {
        const val INCORRECT_PASSWORD = "Esa no es tu contraseña actual. Intenta de nuevo."
    }
}
