package com.vitalcare.serenia.features.socialcompanionship.presentation

enum class AudioPhase {
    READY,
    RECORDING,
    REVIEW
}

data class AudioMessageUiState(
    val phase: AudioPhase = AudioPhase.READY,
    val elapsedSeconds: Int = 0,
    val levels: List<Float> = emptyList(),
    val recordedSeconds: Int = 0,
    val isPlaying: Boolean = false,
    val playbackProgress: Float = 0f,
    val recipients: List<String>? = null,
    val isSending: Boolean = false,
    val error: String? = null,
    val isSent: Boolean = false,
    val isClosed: Boolean = false
) {
    val canSend: Boolean
        get() = recipients == null || recipients.isNotEmpty()
}
