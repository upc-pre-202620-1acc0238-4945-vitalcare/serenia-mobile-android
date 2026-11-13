package com.vitalcare.serenia.features.socialcompanionship.domain

import java.io.File

interface VoicePlayer {

    val positionMillis: Int

    val durationMillis: Int

    fun play(file: File, onCompletion: () -> Unit): Result<Unit>

    fun pause()

    fun resume()

    fun release()
}
