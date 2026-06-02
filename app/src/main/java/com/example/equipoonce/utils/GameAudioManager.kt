package com.example.equipoonce.utils

import android.content.Context
import android.media.MediaPlayer
import com.example.equipoonce.R

class GameAudioManager(private val context: Context) {

    private var backgroundPlayer: MediaPlayer? = null

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

    fun isBackgroundPlaying(): Boolean {
        return backgroundPlayer?.isPlaying == true
    }

    fun stopAllSounds() {
        backgroundPlayer?.stop()
        backgroundPlayer?.release()
        backgroundPlayer = null
    }
}
