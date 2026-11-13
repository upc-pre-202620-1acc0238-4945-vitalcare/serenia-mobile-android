package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.format.formatTime
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.socialcompanionship.application.CancelReminderUseCase
import com.vitalcare.serenia.features.socialcompanionship.application.CompleteReminderUseCase
import com.vitalcare.serenia.features.socialcompanionship.application.GetActiveRemindersUseCase
import com.vitalcare.serenia.features.socialcompanionship.application.PostponeReminderUseCase
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@HiltViewModel
class RemindersViewModel @Inject constructor(
    private val getActiveReminders: GetActiveRemindersUseCase,
    private val postponeReminder: PostponeReminderUseCase,
    private val completeReminder: CompleteReminderUseCase,
    private val cancelReminder: CancelReminderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RemindersUiState())
    val state: StateFlow<RemindersUiState> = _state.asStateFlow()

    private val completingIds = ConcurrentHashMap.newKeySet<String>()
    private var completionJob: Job? = null

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            getActiveReminders().fold(
                onSuccess = { reminders ->
                    _state.update { state ->
                        state.copy(reminders = reminders.filterNot { it.id in completingIds }, loadError = null)
                    }
                },
                onFailure = { exception ->
                    _state.update { it.copy(loadError = exception.toUserMessage()) }
                }
            )
        }
    }

    fun complete(reminderId: String) {
        val reminder = _state.value.reminders?.firstOrNull { it.id == reminderId } ?: return
        sendPendingCompletion()

        completingIds.add(reminder.id)
        _state.update { state ->
            state.copy(
                reminders = state.reminders?.filterNot { it.id == reminder.id },
                completedReminder = reminder,
                message = null,
                actionErrorReminderId = null,
                actionError = null
            )
        }
        completionJob = viewModelScope.launch(Dispatchers.IO) {
            delay(UNDO_WINDOW_MILLIS)
            sendCompletion(reminder)
        }
    }

    fun undoComplete() {
        val reminder = _state.value.completedReminder ?: return
        completionJob?.cancel()
        completionJob = null
        completingIds.remove(reminder.id)

        _state.update { state ->
            state.copy(
                reminders = (state.reminders.orEmpty() + reminder).sortedBy { it.remindAt },
                completedReminder = null
            )
        }
    }

    fun postpone(reminderId: String) {
        if (_state.value.busyReminderId != null) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(busyReminderId = reminderId, actionErrorReminderId = null, actionError = null) }

            postponeReminder(reminderId).fold(
                onSuccess = { postponed ->
                    _state.update { state ->
                        state.copy(
                            reminders = state.reminders
                                ?.map { if (it.id == postponed.id) postponed else it }
                                ?.sortedBy { it.remindAt },
                            busyReminderId = null,
                            message = "Listo, te lo recordamos a las " +
                                "${formatTime(postponed.remindAt.hour, postponed.remindAt.minute)}."
                        )
                    }
                },
                onFailure = { exception ->
                    _state.update {
                        it.copy(
                            busyReminderId = null,
                            actionErrorReminderId = reminderId,
                            actionError = exception.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    fun askToCancel(reminderId: String) {
        if (_state.value.busyReminderId != null) return
        _state.update {
            it.copy(reminderToCancel = reminderId, actionErrorReminderId = null, actionError = null)
        }
    }

    fun keepReminder() {
        if (_state.value.busyReminderId != null) return
        _state.update { it.copy(reminderToCancel = null, actionErrorReminderId = null, actionError = null) }
    }

    fun confirmCancel() {
        val reminderId = _state.value.reminderToCancel ?: return
        if (_state.value.busyReminderId != null) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(busyReminderId = reminderId, actionErrorReminderId = null, actionError = null) }

            cancelReminder(reminderId).fold(
                onSuccess = {
                    _state.update { state ->
                        state.copy(
                            reminders = state.reminders?.filterNot { it.id == reminderId },
                            reminderToCancel = null,
                            busyReminderId = null
                        )
                    }
                },
                onFailure = { exception ->
                    _state.update {
                        it.copy(
                            busyReminderId = null,
                            actionErrorReminderId = reminderId,
                            actionError = exception.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    fun showMessage(message: String) {
        _state.update { it.copy(message = message) }
    }

    fun onMessageShown() {
        _state.update { it.copy(message = null) }
    }

    override fun onCleared() {
        val reminder = _state.value.completedReminder ?: return
        completionJob?.cancel()
        CoroutineScope(Dispatchers.IO).launch { completeReminder(reminder.id) }
    }

    private fun sendPendingCompletion() {
        val reminder = _state.value.completedReminder ?: return
        completionJob?.cancel()
        viewModelScope.launch(Dispatchers.IO) { sendCompletion(reminder) }
    }

    private suspend fun sendCompletion(reminder: SocialReminder) {
        _state.update { state ->
            if (state.completedReminder?.id == reminder.id) state.copy(completedReminder = null) else state
        }

        completeReminder(reminder.id).onFailure { exception ->
            _state.update { state ->
                state.copy(
                    reminders = (state.reminders.orEmpty() + reminder).sortedBy { it.remindAt },
                    actionErrorReminderId = reminder.id,
                    actionError = exception.toUserMessage()
                )
            }
        }
        completingIds.remove(reminder.id)
    }

    private companion object {
        const val UNDO_WINDOW_MILLIS = 6_000L
    }
}
