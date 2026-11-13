package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import android.media.MediaPlayer
import com.vitalcare.serenia.features.socialcompanionship.domain.VoicePlayer
import java.io.File
import javax.inject.Inject

class MediaVoicePlayer @Inject constructor() : VoicePlayer {

    private var player: MediaPlayer? = null

    override val positionMillis: Int
        get() = runCatching { player?.currentPosition ?: 0 }.getOrDefault(0)

    override val durationMillis: Int
        get() = runCatching { player?.duration ?: 0 }.getOrDefault(0)

    override fun play(file: File, onCompletion: () -> Unit): Result<Unit> {
        release()
        return runCatching {
            player = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                setOnCompletionListener { onCompletion() }
                prepare()
                start()
            }
        }.onFailure { release() }
    }

    override fun pause() {
        runCatching { player?.pause() }
    }

    override fun resume() {
        runCatching { player?.start() }
    }

    override fun release() {
        player?.release()
        player = null
    }
}
