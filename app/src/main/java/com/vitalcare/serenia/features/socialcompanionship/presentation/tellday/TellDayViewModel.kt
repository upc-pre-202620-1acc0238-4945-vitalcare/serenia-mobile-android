package com.vitalcare.serenia.features.socialcompanionship.presentation.tellday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.vitalcare.serenia.features.socialcompanionship.domain.RecordingState
import javax.inject.Inject

@HiltViewModel
class TellDayViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TellDayUiState())
    val uiState: StateFlow<TellDayUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun toggleRecording() {
        if (_uiState.value.recordingState == RecordingState.RECORDING) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    fun discard() {
        timerJob?.cancel()
        _uiState.value = TellDayUiState()
    }

    fun sendAudio() {
        // The audio will be sent to the linked family once recording is available
        discard()
    }

    private fun startRecording() {
        timerJob?.cancel()
        _uiState.value = TellDayUiState(recordingState = RecordingState.RECORDING)

        // The seconds are simulated: no audio is captured yet
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { currentState ->
                    currentState.copy(elapsedSeconds = currentState.elapsedSeconds + 1)
                }
            }
        }
    }

    private fun stopRecording() {
        timerJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                // A recording that lasted no time is not worth sending
                recordingState = if (currentState.elapsedSeconds > 0) {
                    RecordingState.RECORDED
                } else {
                    RecordingState.IDLE
                }
            )
        }
    }
}
