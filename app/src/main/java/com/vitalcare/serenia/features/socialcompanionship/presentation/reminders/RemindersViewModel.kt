package com.vitalcare.serenia.features.socialcompanionship.presentation.reminders

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
import com.vitalcare.serenia.features.socialcompanionship.application.CancelReminderUseCase
import com.vitalcare.serenia.features.socialcompanionship.application.CompleteReminderUseCase
import com.vitalcare.serenia.features.socialcompanionship.application.GetRemindersUseCase
import com.vitalcare.serenia.features.socialcompanionship.application.PostponeReminderUseCase
import javax.inject.Inject

@HiltViewModel
class RemindersViewModel @Inject constructor(
    private val getReminders: GetRemindersUseCase,
    private val completeReminder: CompleteReminderUseCase,
    private val postponeReminder: PostponeReminderUseCase,
    private val cancelReminder: CancelReminderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RemindersUiState())
    val uiState: StateFlow<RemindersUiState> = _uiState.asStateFlow()

    private val _events = Channel<RemindersEvent>(Channel.BUFFERED)
    val events: Flow<RemindersEvent> = _events.receiveAsFlow()

    fun loadReminders() {
        viewModelScope.launch {
            getReminders().collect { reminders ->
                _uiState.update { currentState ->
                    currentState.copy(reminders = reminders)
                }
            }
        }
    }

    fun markAsDone(id: Int) {
        viewModelScope.launch {
            completeReminder(id)
        }
    }

    fun postpone(id: Int) {
        viewModelScope.launch {
            postponeReminder(id)
        }
    }

    fun cancel(id: Int) {
        viewModelScope.launch {
            cancelReminder(id).onSuccess {
                _events.send(RemindersEvent.ReminderCancelled)
            }
        }
    }

    fun onReminderSaved() {
        viewModelScope.launch {
            _events.send(RemindersEvent.ReminderSaved)
        }
    }

    init {
        loadReminders()
    }
}
