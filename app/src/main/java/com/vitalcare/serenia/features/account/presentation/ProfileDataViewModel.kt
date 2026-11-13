package com.vitalcare.serenia.features.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.network.ApiException
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.iam.application.GetCurrentUserUseCase
import com.vitalcare.serenia.features.iam.application.UpdateProfileUseCase
import com.vitalcare.serenia.features.iam.application.UploadProfilePhotoUseCase
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
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileDataViewModel @Inject constructor(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val updateProfile: UpdateProfileUseCase,
    private val uploadProfilePhoto: UploadProfilePhotoUseCase
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
                            photoUrl = user.photoUrl,
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

    fun uploadPhoto(photoUri: String) {
        if (_state.value.isUploadingPhoto) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isUploadingPhoto = true, photoError = null, message = null) }

            uploadProfilePhoto(photoUri).fold(
                onSuccess = { user ->
                    _state.update {
                        it.copy(isUploadingPhoto = false, photoUrl = user.photoUrl, message = PHOTO_UPDATED)
                    }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isUploadingPhoto = false, photoError = exception.toPhotoMessage()) }
                }
            )
        }
    }

    fun onMessageShown() {
        _state.update { it.copy(message = null) }
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

    private fun Throwable.toPhotoMessage(): String {
        return if (this is IOException || this is ApiException) toUserMessage() else PHOTO_NOT_READ
    }

    private companion object {
        const val PHOTO_UPDATED = "Listo, cambiamos tu foto."
        const val PHOTO_NOT_READ = "No pudimos abrir esa foto. Elige otra."
    }
}
