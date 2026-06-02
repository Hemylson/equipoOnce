package com.example.equipoonce.utils

import android.content.Context
import android.media.MediaPlayer
import com.example.equipoonce.R

class GameAudioManager(private val context: Context) {

    private var backgroundPlayer: MediaPlayer? = null
    private var spinPlayer: MediaPlayer? = null

    fun playBackground() {
        if (backgroundPlayer == null) {
            backgroundPlayer = MediaPlayer.create(context, R.raw.background_music).apply {
                isLooping = true
                setVolume(0.6f, 0.6f)
                start()
            }
        } else if (backgroundPlayer?.isPlaying == false) {
            backgroundPlayer?.start()
        }
    }

    fun pauseBackground() {
        if (backgroundPlayer?.isPlaying == true) {
            backgroundPlayer?.pause()
        }
    }

    fun resumeBackground() {
        if (backgroundPlayer?.isPlaying == false) {
            backgroundPlayer?.start()
        }
    }

    fun playSpinSound() {
        if (spinPlayer == null) {
            spinPlayer = MediaPlayer.create(context, R.raw.spin_sound).apply {
                isLooping = true
                setVolume(0.8f, 0.8f)
                start()
            }
        } else if (spinPlayer?.isPlaying == false) {
            spinPlayer?.start()
        }
    }

    fun stopSpinSound() {
        spinPlayer?.stop()
        spinPlayer?.release()
        spinPlayer = null
    }

    fun isBackgroundPlaying(): Boolean {
        return backgroundPlayer?.isPlaying == true
    }

    fun stopAllSounds() {
        stopSpinSound()
        backgroundPlayer?.stop()
        backgroundPlayer?.release()
        backgroundPlayer = null
    }
}
