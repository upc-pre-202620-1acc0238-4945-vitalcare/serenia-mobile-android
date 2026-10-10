package com.vitalcare.serenia.features.socialcompanionship.presentation.tellday

import com.vitalcare.serenia.features.socialcompanionship.domain.RecordingState

data class TellDayUiState(
    val recordingState: RecordingState = RecordingState.IDLE,
    val elapsedSeconds: Int = 0
) {
    val formattedTime: String
        get() = "%d:%02d".format(elapsedSeconds / 60, elapsedSeconds % 60)

    val canSend: Boolean
        get() = recordingState == RecordingState.RECORDED
}
