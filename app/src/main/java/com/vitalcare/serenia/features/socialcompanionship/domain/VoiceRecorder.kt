package com.vitalcare.serenia.features.socialcompanionship.domain

import java.io.File

interface VoiceRecorder {

    fun start(): Result<Unit>

    fun stop(): Result<File>

    fun cancel()

    fun currentLevel(): Float
}
