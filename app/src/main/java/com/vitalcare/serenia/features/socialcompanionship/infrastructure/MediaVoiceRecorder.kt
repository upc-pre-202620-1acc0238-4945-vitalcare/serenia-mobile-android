package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.vitalcare.serenia.features.socialcompanionship.domain.VoiceRecorder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class MediaVoiceRecorder @Inject constructor(
    @ApplicationContext private val context: Context
) : VoiceRecorder {

    private var recorder: MediaRecorder? = null
    private val outputFile: File
        get() = File(context.cacheDir, FILE_NAME)

    override fun start(): Result<Unit> {
        cancel()
        return runCatching {
            val mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }
            recorder = mediaRecorder
            mediaRecorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioSamplingRate(SAMPLING_RATE)
                setAudioEncodingBitRate(BIT_RATE)
                setOutputFile(outputFile.absolutePath)
                prepare()
                start()
            }
            Unit
        }.onFailure { release() }
    }

    override fun stop(): Result<File> {
        val mediaRecorder = recorder ?: return Result.failure(IllegalStateException("Not recording"))
        return runCatching {
            mediaRecorder.stop()
            outputFile
        }.also { release() }
    }

    override fun cancel() {
        recorder?.let { mediaRecorder ->
            runCatching { mediaRecorder.stop() }
        }
        release()
        outputFile.delete()
    }

    override fun currentLevel(): Float {
        val amplitude = runCatching { recorder?.maxAmplitude ?: 0 }.getOrDefault(0)
        return (amplitude / MAX_AMPLITUDE).coerceIn(0f, 1f)
    }

    private fun release() {
        recorder?.release()
        recorder = null
    }

    private companion object {
        const val FILE_NAME = "voice_message.m4a"
        const val SAMPLING_RATE = 44_100
        const val BIT_RATE = 64_000
        const val MAX_AMPLITUDE = 20_000f
    }
}
