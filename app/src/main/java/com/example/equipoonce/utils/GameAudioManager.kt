package com.example.equipoonce.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import timber.log.Timber
import com.example.equipoonce.R

class GameAudioManager(private val context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var backgroundPlayer: MediaPlayer? = null
    private var spinPlayer: MediaPlayer? = null

    private fun requestAudioFocus() {
        @Suppress("DEPRECATION")
        audioManager.requestAudioFocus(
            null,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
    }

    fun playBackground() {
        if (backgroundPlayer == null) {
            requestAudioFocus()
            backgroundPlayer = MediaPlayer.create(context, R.raw.background_music)?.apply {
                isLooping = true
                setVolume(1.0f, 1.0f)
                start()
            } ?: run {
                Timber.e("Error: no se pudo crear el reproductor de música de fondo")
                null
            }
        } else if (backgroundPlayer?.isPlaying == false) {
            runCatching { backgroundPlayer?.start() }
                .onFailure { Timber.e(it, "Error al reanudar música de fondo") }
        }
    }

    fun pauseBackground() {
        runCatching {
            if (backgroundPlayer?.isPlaying == true) {
                backgroundPlayer?.pause()
            }
        }
    }

    fun resumeBackground() {
        runCatching {
            if (backgroundPlayer?.isPlaying == false) {
                backgroundPlayer?.start()
            }
        }
    }

    fun playSpinSound() {
        if (spinPlayer == null) {
            spinPlayer = MediaPlayer.create(context, R.raw.spin_sound)?.apply {
                isLooping = true
                setVolume(1.0f, 1.0f)
                start()
            }
        } else if (spinPlayer?.isPlaying == false) {
            runCatching { spinPlayer?.start() }
        }
    }

    fun stopSpinSound() {
        runCatching { spinPlayer?.pause() }
        releasePlayer(spinPlayer)
        spinPlayer = null
    }

    fun isBackgroundPlaying(): Boolean =
        runCatching { backgroundPlayer?.isPlaying == true }.getOrDefault(false)

    fun stopAllSounds() {
        releasePlayer(spinPlayer)
        spinPlayer = null
        releasePlayer(backgroundPlayer)
        backgroundPlayer = null
    }

    private fun releasePlayer(player: MediaPlayer?) {
        player ?: return
        runCatching {
            if (player.isPlaying) player.stop()
            player.release()
        }
    }
}
