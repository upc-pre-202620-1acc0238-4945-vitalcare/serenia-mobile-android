package com.vitalcare.serenia.features.socialcompanionship.domain

import java.io.File

interface AudioMessageRepository {

    suspend fun recordAudioMessage(file: File, durationSeconds: Int): Result<AudioMessage>

    suspend fun shareAudioMessage(audioMessageId: String): Result<AudioMessage>

    suspend fun discardAudioMessage(audioMessageId: String): Result<Unit>
}
