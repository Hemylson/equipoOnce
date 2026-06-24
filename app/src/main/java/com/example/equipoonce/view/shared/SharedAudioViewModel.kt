package com.example.equipoonce.view.shared

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import com.example.equipoonce.utils.GameAudioManager
import javax.inject.Inject

@HiltViewModel
class SharedAudioViewModel @Inject constructor(private val audioManager: GameAudioManager) : ViewModel() {

    private val _isAudioOn = MutableStateFlow(true)
    val isAudioOn: StateFlow<Boolean> = _isAudioOn

    private var audioWasPlayingBeforeSpin = false
    private var isUserMuted = false
    private var isCounting = false

    fun startBackground() {
        if (!isUserMuted) audioManager.playBackground()
    }

    fun pauseBackground() {
        audioManager.pauseBackground()
        audioManager.stopSpinSound()
    }

    fun resumeIfNeeded() {
        if (!isCounting && !isUserMuted) audioManager.resumeBackground()
    }

    fun pauseForNavigation() {
        audioManager.pauseBackground()
    }

    fun toggleAudio() {
        val estabaEncendido = _isAudioOn.value == true
        if (estabaEncendido) {
            isUserMuted = true
            _isAudioOn.value = false
            audioManager.pauseBackground()
            Timber.d("Audio silenciado por el usuario")
        } else {
            isUserMuted = false
            _isAudioOn.value = true
            if (!isCounting) audioManager.resumeBackground()
            Timber.d("Audio activado por el usuario")
        }
    }

    fun onSpinStart() {
        isCounting = true
        audioWasPlayingBeforeSpin = audioManager.isBackgroundPlaying()
        audioManager.pauseBackground()
        audioManager.playSpinSound()
        Timber.d("Giro iniciado — audio fondo pausado, sonido giro reproduciendo")
    }

    fun onSpinEnd() {
        audioManager.stopSpinSound()
        Timber.d("Giro terminado — sonido giro detenido")
    }

    fun onSpinDialogClosed() {
        isCounting = false
        if (audioWasPlayingBeforeSpin && !isUserMuted) audioManager.resumeBackground()
        Timber.d("Diálogo cerrado — audio fondo ${if (audioWasPlayingBeforeSpin && !isUserMuted) "reanudado" else "permanece pausado"}")
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.stopAllSounds()
    }
}
