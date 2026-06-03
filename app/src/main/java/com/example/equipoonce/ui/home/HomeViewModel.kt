package com.example.equipoonce.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.utils.IAudioManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

data class SpinParams(val targetAngle: Float, val durationMs: Long) {
    init {
        require(durationMs > 0) { "La duración debe ser positiva" }
        require(targetAngle.isFinite()) { "El ángulo debe ser un número válido" }
    }
}

sealed class SpinUiState {
    object Idle : SpinUiState()
    data class Spinning(val params: SpinParams) : SpinUiState()
    data class Counting(val value: Int) : SpinUiState()
    object DialogReady : SpinUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val audioManager: IAudioManager
) : ViewModel() {

    companion object {
        private const val MIN_SPIN_DURATION_MS = 4000L
        private const val MAX_SPIN_DURATION_MS = 6000L
        private const val MIN_FULL_SPINS = 5
        private const val MAX_FULL_SPINS = 8
        private const val FULL_DEGREES = 360
        private const val COUNTDOWN_START = 3
    }

    // Estado de giro consolidado
    private val _spinUiState = MutableStateFlow<SpinUiState>(SpinUiState.Idle)
    val spinUiState: StateFlow<SpinUiState> = _spinUiState

    // Estado de audio — independiente del giro
    private val _isAudioOn = MutableStateFlow(true)
    val isAudioOn: StateFlow<Boolean> = _isAudioOn

    // Estado interno del giro
    private var currentRotation = 0f
    private var audioWasPlaying = false
    private var isUserMuted = false

    private val isCounting get() = _spinUiState.value !is SpinUiState.Idle

    // ── Audio ──────────────────────────────────────────────────────────────

    fun startBackground() {
        if (!isUserMuted) audioManager.playBackground()
    }

    fun onFragmentResume() {
        if (!isCounting && !isUserMuted) audioManager.resumeBackground()
    }

    fun onFragmentPause() {
        audioManager.pauseBackground()
        audioManager.stopSpinSound()
    }

    fun onFragmentDestroy() {
        audioManager.pauseBackground()
    }

    fun toggleAudio() {
        val estabaEncendido = _isAudioOn.value == true
        if (estabaEncendido) {
            isUserMuted = true
            _isAudioOn.value = false
            audioManager.pauseBackground()
        } else {
            isUserMuted = false
            _isAudioOn.value = true
            if (!isCounting) audioManager.resumeBackground()
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
        if (isCounting) return

        val durationMs = (MIN_SPIN_DURATION_MS..MAX_SPIN_DURATION_MS).random()
        val direction = if (Random.nextBoolean()) 1 else -1
        val fullSpins = (MIN_FULL_SPINS..MAX_FULL_SPINS).random()
        val extraAngle = Random.nextInt(0, FULL_DEGREES)
        val targetAngle = currentRotation + direction * (fullSpins * FULL_DEGREES + extraAngle)

        _spinUiState.value = SpinUiState.Spinning(SpinParams(targetAngle, durationMs))

        viewModelScope.launch {
            delay(durationMs)
            currentRotation = ((targetAngle % 360f) + 360f) % 360f

            for (value in COUNTDOWN_START downTo 0) {
                _spinUiState.value = SpinUiState.Counting(value)
                delay(1000)
            }

            _spinUiState.value = SpinUiState.DialogReady
        }
    }

    fun onDialogShown() {
        _spinUiState.value = SpinUiState.Idle
    }

    fun onDialogClosed() {
        if (audioWasPlaying && !isUserMuted) audioManager.resumeBackground()
    }
}
