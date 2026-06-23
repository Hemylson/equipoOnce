package com.example.equipoonce.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

data class SpinParams(val targetAngle: Float, val durationMs: Long) {
    init {
        require(durationMs > 0) { "La duración debe ser positiva" }
        require(targetAngle.isFinite()) { "El ángulo debe ser un número válido" }
    }
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    companion object {
        private const val MIN_SPIN_DURATION_MS = 4000L
        private const val MAX_SPIN_DURATION_MS = 6000L
        private const val MIN_FULL_SPINS = 5
        private const val MAX_FULL_SPINS = 8
        private const val FULL_DEGREES = 360
        private const val COUNTDOWN_START = 3
    }

    private val _contador = MutableStateFlow<Int?>(null)
    val contador: StateFlow<Int?> = _contador

    private val _isButtonVisible = MutableStateFlow(true)
    val isButtonVisible: StateFlow<Boolean> = _isButtonVisible

    private val _spinEvent = MutableStateFlow<SpinParams?>(null)
    val spinEvent: StateFlow<SpinParams?> = _spinEvent

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent: StateFlow<Boolean> = _logoutEvent

    /** HU 3.0 — cierra la sesión de Firebase y dispara la navegación al Login. */
    fun cerrarSesion() {
        authRepository.logout()
        Timber.d("Sesión cerrada por el usuario")
        _logoutEvent.value = true
    }

    fun onLogoutHandled() { _logoutEvent.value = false }

    private val _counting = MutableStateFlow(false)
    private var currentRotation = 0f

    fun onPresionameClicked() {
        if (_counting.value) return
        Timber.d("Botella girada — iniciando giro aleatorio")

        _counting.value = true
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

    fun onSpinEventConsumed() { _spinEvent.value = null }

    fun onDialogShown() { _showDialog.value = false }

    fun onDialogClosed() {
        _counting.value = false
        Timber.d("Diálogo cerrado — juego listo para nueva partida")
    }

    internal fun isCounting(): Boolean = _counting.value
}
