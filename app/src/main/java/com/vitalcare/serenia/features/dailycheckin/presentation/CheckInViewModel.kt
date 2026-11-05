package com.vitalcare.serenia.features.dailycheckin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.dailycheckin.application.ActivateDailyPauseUseCase
import com.vitalcare.serenia.features.dailycheckin.application.AnswerCheckInUseCase
import com.vitalcare.serenia.features.dailycheckin.application.GetCheckInPreferencesUseCase
import com.vitalcare.serenia.features.dailycheckin.application.GetTodayCheckInUseCase
import com.vitalcare.serenia.features.dailycheckin.application.ResumeDailyCheckInUseCase
import com.vitalcare.serenia.features.dailycheckin.domain.CheckIn
import com.vitalcare.serenia.features.dailycheckin.domain.CheckInStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckInViewModel @Inject constructor(
    private val getTodayCheckIn: GetTodayCheckInUseCase,
    private val answerCheckIn: AnswerCheckInUseCase,
    private val getCheckInPreferences: GetCheckInPreferencesUseCase,
    private val activateDailyPause: ActivateDailyPauseUseCase,
    private val resumeDailyCheckIn: ResumeDailyCheckInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CheckInUiState())
    val state: StateFlow<CheckInUiState> = _state.asStateFlow()

    fun loadCheckIn() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(content = CheckInContent.Loading, actionError = null) }
            getCheckInPreferences()
            refreshCheckIn()
        }
    }

    fun answer(answer: MoodAnswer) {
        val content = _state.value.content as? CheckInContent.Pending ?: return
        if (_state.value.isSubmitting) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isSubmitting = true, actionError = null) }

            answerCheckIn(content.checkInId, answer.mood).fold(
                onSuccess = { checkIn ->
                    _state.update { it.copy(content = checkIn.toContent(), isSubmitting = false) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isSubmitting = false, actionError = exception.toUserMessage()) }
                }
            )
        }
    }

    fun skipToday() {
        changePause { activateDailyPause() }
    }

    fun resumeToday() {
        changePause { resumeDailyCheckIn() }
    }

    private fun changePause(change: suspend () -> Result<*>) {
        if (_state.value.isSubmitting) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isSubmitting = true, actionError = null) }

            change().fold(
                onSuccess = {
                    refreshCheckIn()
                    _state.update { it.copy(isSubmitting = false) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isSubmitting = false, actionError = exception.toUserMessage()) }
                }
            )
        }
    }

    private suspend fun refreshCheckIn() {
        getTodayCheckIn().fold(
            onSuccess = { checkIn ->
                _state.update { it.copy(content = checkIn?.toContent() ?: CheckInContent.Unavailable) }
            },
            onFailure = { exception ->
                _state.update { it.copy(content = CheckInContent.Failed(exception.toUserMessage())) }
            }
        )
    }

    private fun CheckIn.toContent(): CheckInContent {
        return when {
            paused || status == CheckInStatus.SKIPPED -> CheckInContent.Paused
            status == CheckInStatus.ANSWERED -> CheckInContent.Answered(mood?.toAnswer() ?: MoodAnswer.GOOD)
            status == CheckInStatus.MISSED -> CheckInContent.Missed
            else -> CheckInContent.Pending(checkInId = id, question = questionText)
        }
    }

    init {
        loadCheckIn()
    }
}
