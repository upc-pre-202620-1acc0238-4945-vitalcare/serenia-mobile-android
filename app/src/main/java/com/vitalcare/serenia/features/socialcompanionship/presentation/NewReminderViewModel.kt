package com.vitalcare.serenia.features.socialcompanionship.presentation.newreminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.vitalcare.serenia.features.socialcompanionship.application.CreateReminderUseCase
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderSchedule
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderType
import javax.inject.Inject

@HiltViewModel
class NewReminderViewModel @Inject constructor(
    private val createReminder: CreateReminderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewReminderUiState())
    val uiState: StateFlow<NewReminderUiState> = _uiState.asStateFlow()

    private val _events = Channel<NewReminderEvent>(Channel.BUFFERED)
    val events: Flow<NewReminderEvent> = _events.receiveAsFlow()

    fun selectType(type: ReminderType) {
        _uiState.update { currentState ->
            currentState.copy(selectedType = type)
        }
    }

    fun selectSchedule(schedule: ReminderSchedule) {
        _uiState.update { currentState ->
            currentState.copy(selectedSchedule = schedule)
        }
    }

    fun saveReminder() {
        // Avoids saving the same reminder twice when the button is pressed repeatedly
        if (_uiState.value.isSaving) return

        _uiState.update { currentState ->
            currentState.copy(isSaving = true)
        }

        viewModelScope.launch {
            val currentState = _uiState.value
            val result = createReminder(currentState.selectedType, currentState.selectedSchedule)

            result.fold(
                onSuccess = {
                    _events.send(NewReminderEvent.ReminderSaved)
                },
                onFailure = {
                    _uiState.update { state ->
                        state.copy(isSaving = false)
                    }
                }
            )
        }
    }
}
