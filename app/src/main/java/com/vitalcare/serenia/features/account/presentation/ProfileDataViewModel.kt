package com.vitalcare.serenia.features.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.iam.application.GetCurrentUserUseCase
import com.vitalcare.serenia.features.iam.application.UpdateProfileUseCase
import com.vitalcare.serenia.features.iam.domain.BirthDate
import com.vitalcare.serenia.features.iam.domain.PhoneNumber
import com.vitalcare.serenia.features.iam.presentation.fullNameErrorFor
import com.vitalcare.serenia.features.iam.presentation.phoneNumberErrorFor
import com.vitalcare.serenia.features.iam.presentation.toPhoneDigits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDataViewModel @Inject constructor(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val updateProfile: UpdateProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileDataUiState())
    val state: StateFlow<ProfileDataUiState> = _state.asStateFlow()

    fun onFullNameChange(fullName: String) {
        _state.update { it.copy(fullName = fullName, fullNameError = null) }
    }

    fun onBirthDateChange(birthDate: BirthDate) {
        _state.update { it.copy(birthDate = birthDate) }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _state.update { it.copy(phoneNumber = phoneNumber.toPhoneDigits(), phoneNumberError = null) }
    }

    fun loadUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, formError = null) }

            getCurrentUser().fold(
                onSuccess = { user ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            fullName = user.fullName,
                            birthDate = user.birthDate,
                            phoneNumber = user.phoneNumber?.let(PhoneNumber::toLocal).orEmpty()
                        )
                    }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false, formError = exception.toUserMessage()) }
                }
            )
        }
    }

    fun save() {
        val currentState = _state.value
        if (currentState.isSaving || currentState.isLoading) return

        val fullNameError = fullNameErrorFor(currentState.fullName)
        val phoneNumberError = phoneNumberErrorFor(currentState.phoneNumber)

        if (fullNameError != null || phoneNumberError != null) {
            _state.update {
                it.copy(fullNameError = fullNameError, phoneNumberError = phoneNumberError, formError = null)
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isSaving = true, formError = null) }

            updateProfile(
                fullName = currentState.fullName,
                phoneNumber = currentState.phoneNumber.ifEmpty { null }?.let(PhoneNumber::fromLocal),
                birthDate = currentState.birthDate
            ).fold(
                onSuccess = {
                    _state.update { it.copy(isSaving = false, isSaved = true) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isSaving = false, formError = exception.toUserMessage()) }
                }
            )
        }
    }

    init {
        loadUser()
    }
}
