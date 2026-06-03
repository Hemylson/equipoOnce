package com.example.equipoonce.utils

/**
 * Contrato para la gestión de audio del juego.
 * Permite sustituir implementaciones (MediaPlayer, ExoPlayer, mock en tests).
 */
interface IAudioManager {
    fun playBackground()
    fun pauseBackground()
    fun resumeBackground()
    fun playSpinSound()
    fun stopSpinSound()
    fun isBackgroundPlaying(): Boolean
    fun stopAllSounds()
}
