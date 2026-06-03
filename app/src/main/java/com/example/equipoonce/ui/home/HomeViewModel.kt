package com.example.equipoonce.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.utils.GameAudioManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class SpinParams(val targetAngle: Float, val durationMs: Long) {
    init {
        require(durationMs > 0) { "La duración debe ser positiva" }
        require(targetAngle.isFinite()) { "El ángulo debe ser un número válido" }
    }
}

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val MIN_SPIN_DURATION_MS = 4000L
        private const val MAX_SPIN_DURATION_MS = 6000L
        private const val MIN_FULL_SPINS = 5
        private const val MAX_FULL_SPINS = 8
        private const val FULL_DEGREES = 360
        private const val COUNTDOWN_START = 3
    }

    private val audioManager = GameAudioManager(application)

    private val _contador = MutableLiveData<Int?>(null)
    val contador: LiveData<Int?> = _contador

    private val _isButtonVisible = MutableLiveData(true)
    val isButtonVisible: LiveData<Boolean> = _isButtonVisible

    private val _spinEvent = MutableLiveData<SpinParams?>(null)
    val spinEvent: LiveData<SpinParams?> = _spinEvent

    private val _showDialog = MutableLiveData(false)
    val showDialog: LiveData<Boolean> = _showDialog

    private val _isAudioOn = MutableLiveData(true)
    val isAudioOn: LiveData<Boolean> = _isAudioOn

    private var counting = false
    private var currentRotation = 0f
    private var audioWasPlaying = false
    private var isUserMuted = false

    // ── Audio ──────────────────────────────────────────────────────────────

    fun startBackground() {
        if (!isUserMuted) audioManager.playBackground()
    }

    fun onFragmentResume() {
        if (!counting && !isUserMuted) {
            audioManager.resumeBackground()
        }
    }

    fun onFragmentPause() {
        audioManager.pauseBackground()
        audioManager.stopSpinSound()
    }

    fun onFragmentDestroy() {
        // Solo pausa — el MediaPlayer mantiene su posición para reanudar al volver
        audioManager.pauseBackground()
    }

    fun toggleAudio() {
        val audioEstabaPrendido = _isAudioOn.value == true
        if (audioEstabaPrendido) {
            isUserMuted = true
            _isAudioOn.value = false
            audioManager.pauseBackground()
        } else {
            isUserMuted = false
            _isAudioOn.value = true
            if (!counting) audioManager.resumeBackground()
        }
    }

    fun onSpinStart() {
        audioWasPlaying = audioManager.isBackgroundPlaying()
        audioManager.pauseBackground()
        audioManager.playSpinSound()
    }

    fun onSpinEnd() {
        audioManager.stopSpinSound()
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.stopAllSounds()
    }

    // ── Giro ───────────────────────────────────────────────────────────────

    fun onPresionameClicked() {
        if (counting) return

        counting = true
        _isButtonVisible.value = false
        _contador.value = null

        val durationMs = (MIN_SPIN_DURATION_MS..MAX_SPIN_DURATION_MS).random()
        val direction = if (Random.nextBoolean()) 1 else -1
        val fullSpins = (MIN_FULL_SPINS..MAX_FULL_SPINS).random()
        val extraAngle = Random.nextInt(0, FULL_DEGREES)
        val targetAngle = currentRotation + direction * (fullSpins * FULL_DEGREES + extraAngle)

        _spinEvent.value = SpinParams(targetAngle, durationMs)

        viewModelScope.launch {
            delay(durationMs)
            currentRotation = ((targetAngle % 360f) + 360f) % 360f

            for (value in COUNTDOWN_START downTo 0) {
                _contador.value = value
                delay(1000)
            }

            _contador.value = null
            _isButtonVisible.value = true
            _showDialog.value = true
        }
    }

    fun clearSpinEvent() { _spinEvent.value = null }

    fun onDialogShown() { _showDialog.value = false }

    fun onDialogClosed() {
        counting = false
        if (audioWasPlaying && !isUserMuted) audioManager.resumeBackground()
    }

    fun isCounting(): Boolean = counting
}
