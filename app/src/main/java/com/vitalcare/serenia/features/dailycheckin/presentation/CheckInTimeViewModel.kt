package com.vitalcare.serenia.features.dailycheckin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.dailycheckin.application.GetCheckInPreferencesUseCase
import com.vitalcare.serenia.features.dailycheckin.application.ScheduleCheckInUseCase
import com.vitalcare.serenia.features.dailycheckin.domain.ReminderTime
import com.vitalcare.serenia.features.dailycheckin.domain.ReminderTimeOutOfRangeException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckInTimeViewModel @Inject constructor(
    private val getCheckInPreferences: GetCheckInPreferencesUseCase,
    private val scheduleCheckIn: ScheduleCheckInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CheckInTimeUiState())
    val state: StateFlow<CheckInTimeUiState> = _state.asStateFlow()

    fun openPicker() {
        _state.update { it.copy(isPickerVisible = true) }
    }

    fun closePicker() {
        _state.update { it.copy(isPickerVisible = false) }
    }

    fun onTimeChosen(hour: Int, minute: Int) {
        val reminderTime = ReminderTime(hour, minute)
        _state.update {
            it.copy(
                reminderTime = reminderTime,
                isPickerVisible = false,
                timeError = if (reminderTime.isWithinAllowedRange) null else OUT_OF_RANGE,
                formError = null
            )
        }
    }

    fun onContinued() {
        _state.update { it.copy(isSaved = false) }
    }

    fun saveTime() {
        val currentState = _state.value
        if (currentState.isSaving || currentState.timeError != null) return

        if (currentState.reminderTime == currentState.savedTime) {
            _state.update { it.copy(isSaved = true) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isSaving = true, formError = null) }

            scheduleCheckIn(currentState.reminderTime).fold(
                onSuccess = { preferences ->
                    _state.update { it.copy(isSaving = false, savedTime = preferences.reminderTime, isSaved = true) }
                },
                onFailure = { exception ->
                    if (exception is ReminderTimeOutOfRangeException) {
                        _state.update { it.copy(isSaving = false, timeError = OUT_OF_RANGE) }
                    } else {
                        _state.update { it.copy(isSaving = false, formError = exception.toUserMessage()) }
                    }
                }
            )
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch(Dispatchers.IO) {
            getCheckInPreferences().onSuccess { preferences ->
                _state.update {
                    it.copy(reminderTime = preferences.reminderTime, savedTime = preferences.reminderTime)
                }
            }
        }
    }

    init {
        loadPreferences()
    }

    private companion object {
        const val OUT_OF_RANGE = "Elige una hora entre las 6:00 a. m. y las 8:00 p. m."
    }
}
