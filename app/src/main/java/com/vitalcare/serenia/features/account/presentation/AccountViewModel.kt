package com.vitalcare.serenia.features.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.appearance.AppearanceStorage
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.dailycheckin.application.GetCheckInPreferencesUseCase
import com.vitalcare.serenia.features.dailycheckin.application.ObserveCheckInPreferencesUseCase
import com.vitalcare.serenia.features.dailycheckin.application.ScheduleCheckInUseCase
import com.vitalcare.serenia.features.dailycheckin.application.UpdateSimplifiedModeUseCase
import com.vitalcare.serenia.features.dailycheckin.domain.ReminderTime
import com.vitalcare.serenia.features.dailycheckin.domain.ReminderTimeOutOfRangeException
import com.vitalcare.serenia.features.iam.application.GetCurrentUserUseCase
import com.vitalcare.serenia.features.iam.application.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getCheckInPreferences: GetCheckInPreferencesUseCase,
    private val observeCheckInPreferences: ObserveCheckInPreferencesUseCase,
    private val scheduleCheckIn: ScheduleCheckInUseCase,
    private val updateSimplifiedMode: UpdateSimplifiedModeUseCase,
    private val signOut: SignOutUseCase,
    private val appearanceStorage: AppearanceStorage
) : ViewModel() {

    private val _state = MutableStateFlow(AccountUiState())
    val state: StateFlow<AccountUiState> = _state.asStateFlow()

    fun openPicker() {
        _state.update { it.copy(isPickerVisible = true) }
    }

    fun closePicker() {
        _state.update { it.copy(isPickerVisible = false) }
    }

    fun onTimeChosen(hour: Int, minute: Int) {
        val reminderTime = ReminderTime(hour, minute)
        _state.update { it.copy(isPickerVisible = false) }

        if (!reminderTime.isWithinAllowedRange) {
            _state.update { it.copy(chosenTime = reminderTime, timeError = OUT_OF_RANGE) }
            return
        }
        if (reminderTime == _state.value.reminderTime) {
            _state.update { it.copy(chosenTime = null, timeError = null) }
            return
        }
        if (_state.value.isSavingTime) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(chosenTime = reminderTime, timeError = null, isSavingTime = true) }

            scheduleCheckIn(reminderTime).fold(
                onSuccess = { preferences ->
                    _state.update {
                        it.copy(reminderTime = preferences.reminderTime, chosenTime = null, isSavingTime = false)
                    }
                },
                onFailure = { exception ->
                    val message = if (exception is ReminderTimeOutOfRangeException) OUT_OF_RANGE else exception.toUserMessage()
                    _state.update { it.copy(timeError = message, isSavingTime = false) }
                }
            )
        }
    }

    fun changeSimplifiedMode(enabled: Boolean) {
        if (_state.value.isSavingSimplifiedMode) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(simplifiedMode = enabled, isSavingSimplifiedMode = true, appearanceError = null)
            }

            updateSimplifiedMode(enabled).fold(
                onSuccess = { preferences ->
                    _state.update {
                        it.copy(simplifiedMode = preferences.simplifiedMode, isSavingSimplifiedMode = false)
                    }
                },
                onFailure = { exception ->
                    _state.update {
                        it.copy(
                            simplifiedMode = !enabled,
                            isSavingSimplifiedMode = false,
                            appearanceError = exception.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    fun changeHighContrast(enabled: Boolean) {
        _state.update { it.copy(highContrast = enabled) }
        viewModelScope.launch {
            appearanceStorage.setHighContrast(enabled)
        }
    }

    fun askToSignOut() {
        _state.update { it.copy(isConfirmingSignOut = true, signOutError = null) }
    }

    fun cancelSignOut() {
        if (_state.value.isSigningOut) return
        _state.update { it.copy(isConfirmingSignOut = false, signOutError = null) }
    }

    fun confirmSignOut() {
        if (_state.value.isSigningOut) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isSigningOut = true, signOutError = null) }

            signOut().onFailure { exception ->
                _state.update { it.copy(isSigningOut = false, signOutError = exception.toUserMessage()) }
            }
        }
    }

    fun loadUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUser().onSuccess { user ->
                _state.update { it.copy(fullName = user.fullName, photoUrl = user.photoUrl) }
            }
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch(Dispatchers.IO) {
            getCheckInPreferences().onFailure { exception ->
                _state.update { it.copy(timeError = exception.toUserMessage()) }
            }
        }
    }

    private fun observePreferences() {
        viewModelScope.launch {
            observeCheckInPreferences().collect { preferences ->
                if (preferences == null) return@collect
                _state.update { state ->
                    state.copy(
                        reminderTime = preferences.reminderTime,
                        simplifiedMode = if (state.isSavingSimplifiedMode) state.simplifiedMode else preferences.simplifiedMode,
                        timeError = if (state.chosenTime == null) null else state.timeError
                    )
                }
            }
        }
    }

    private fun observeHighContrast() {
        viewModelScope.launch {
            appearanceStorage.highContrast.collect { enabled ->
                _state.update { it.copy(highContrast = enabled) }
            }
        }
    }

    init {
        observeHighContrast()
        observePreferences()
        loadPreferences()
    }

    private companion object {
        const val OUT_OF_RANGE = "Elige una hora entre las 6:00 a. m. y las 8:00 p. m."
    }
}
