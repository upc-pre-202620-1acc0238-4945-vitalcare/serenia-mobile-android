package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.carecircle.presentation.FamilyMembersProvider
import com.vitalcare.serenia.features.socialcompanionship.application.SendAudioMessageUseCase
import com.vitalcare.serenia.features.socialcompanionship.domain.VoicePlayer
import com.vitalcare.serenia.features.socialcompanionship.domain.VoiceRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class AudioMessageViewModel @Inject constructor(
    private val recorder: VoiceRecorder,
    private val player: VoicePlayer,
    private val sendAudioMessage: SendAudioMessageUseCase,
    private val getFamilyMembers: FamilyMembersProvider
) : ViewModel() {

    private val _state = MutableStateFlow(AudioMessageUiState())
    val state: StateFlow<AudioMessageUiState> = _state.asStateFlow()

    private var recordedFile: File? = null
    private var recordingJob: Job? = null
    private var playbackJob: Job? = null
    private var isPlaybackStarted = false
    private var recordingStartedAt = 0L

    init {
        loadRecipients()
    }

    fun startRecording() {
        if (_state.value.phase == AudioPhase.RECORDING) return

        recorder.start().onFailure {
            _state.update { it.copy(error = MICROPHONE_ERROR) }
            return
        }
        _state.update {
            it.copy(phase = AudioPhase.RECORDING, elapsedSeconds = 0, levels = emptyList(), error = null)
        }

        recordingStartedAt = System.currentTimeMillis()
        recordingJob = viewModelScope.launch {
            while (isActive) {
                delay(TICK_MILLIS)
                val elapsedMillis = System.currentTimeMillis() - recordingStartedAt
                val level = recorder.currentLevel()
                _state.update {
                    it.copy(
                        elapsedSeconds = (elapsedMillis / 1_000).toInt(),
                        levels = (it.levels + level).takeLast(WAVE_BARS)
                    )
                }
                if (elapsedMillis >= MAX_DURATION_MILLIS) {
                    finishRecording(elapsedMillis)
                    break
                }
            }
        }
    }

    fun stopRecording() {
        if (_state.value.phase != AudioPhase.RECORDING) return
        recordingJob?.cancel()
        finishRecording(System.currentTimeMillis() - recordingStartedAt)
    }

    fun cancelRecording() {
        recordingJob?.cancel()
        recorder.cancel()
        close()
    }

    fun togglePlayback() {
        val file = recordedFile ?: return

        if (_state.value.isPlaying) {
            player.pause()
            playbackJob?.cancel()
            _state.update { it.copy(isPlaying = false) }
            return
        }

        if (isPlaybackStarted) {
            player.resume()
        } else {
            player.play(file, onCompletion = ::onPlaybackCompleted).onFailure {
                _state.update { it.copy(error = PLAYBACK_ERROR) }
                return
            }
            isPlaybackStarted = true
        }
        _state.update { it.copy(isPlaying = true, error = null) }
        trackPlayback()
    }

    fun recordAgain() {
        stopPlayback()
        deleteRecording()
        _state.update {
            it.copy(phase = AudioPhase.READY, elapsedSeconds = 0, levels = emptyList(), recordedSeconds = 0, error = null)
        }
    }

    fun discard() {
        stopPlayback()
        deleteRecording()
        close()
    }

    fun send() {
        val file = recordedFile ?: return
        if (_state.value.isSending) return
        stopPlayback()

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isSending = true, error = null) }

            sendAudioMessage(file, _state.value.recordedSeconds).fold(
                onSuccess = {
                    deleteRecording()
                    _state.update { it.copy(isSending = false, isSent = true) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isSending = false, error = exception.toUserMessage()) }
                }
            )
        }
    }

    fun onExitHandled() {
        _state.update { it.copy(isSent = false, isClosed = false) }
    }

    override fun onCleared() {
        recordingJob?.cancel()
        recorder.cancel()
        player.release()
        deleteRecording()
    }

    private fun finishRecording(elapsedMillis: Long) {
        recorder.stop().fold(
            onSuccess = { file ->
                recordedFile = file
                isPlaybackStarted = false
                _state.update {
                    it.copy(
                        phase = AudioPhase.REVIEW,
                        recordedSeconds = (elapsedMillis / 1_000f).roundToInt().coerceAtLeast(1),
                        playbackProgress = 0f,
                        isPlaying = false
                    )
                }
            },
            onFailure = {
                _state.update {
                    it.copy(phase = AudioPhase.READY, elapsedSeconds = 0, levels = emptyList(), error = TOO_SHORT_ERROR)
                }
            }
        )
    }

    private fun trackPlayback() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (isActive) {
                val duration = player.durationMillis
                val progress = if (duration > 0) player.positionMillis / duration.toFloat() else 0f
                _state.update { it.copy(playbackProgress = progress.coerceIn(0f, 1f)) }
                delay(TICK_MILLIS)
            }
        }
    }

    private fun onPlaybackCompleted() {
        playbackJob?.cancel()
        player.release()
        isPlaybackStarted = false
        _state.update { it.copy(isPlaying = false, playbackProgress = 0f) }
    }

    private fun stopPlayback() {
        playbackJob?.cancel()
        player.release()
        isPlaybackStarted = false
        _state.update { it.copy(isPlaying = false, playbackProgress = 0f) }
    }

    private fun deleteRecording() {
        recordedFile?.delete()
        recordedFile = null
    }

    private fun close() {
        _state.update { it.copy(isClosed = true) }
    }

    private fun loadRecipients() {
        viewModelScope.launch(Dispatchers.IO) {
            getFamilyMembers().onSuccess { members ->
                _state.update { it.copy(recipients = members.map { member -> member.firstName }) }
            }
        }
    }

    private companion object {
        const val TICK_MILLIS = 150L
        const val MAX_DURATION_MILLIS = 120_000L
        const val WAVE_BARS = 26
        const val MICROPHONE_ERROR = "No pudimos usar el micrófono. Intenta de nuevo."
        const val PLAYBACK_ERROR = "No pudimos reproducir tu audio. Intenta de nuevo."
        const val TOO_SHORT_ERROR = "La grabación fue muy corta. Intenta de nuevo."
    }
}
