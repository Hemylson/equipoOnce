package com.example.equipoonce.view.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    companion object {
        const val MAX_EMAIL_LENGTH = 40
        private const val MIN_PASSWORD_LENGTH = 6
        private const val MAX_PASSWORD_LENGTH = 10
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        // CA HU 2.0 — Criterios 9 y 13: textos exactos del Toast.
        const val ERROR_LOGIN = "Login incorrecto"
        const val ERROR_REGISTRO = "Error en el registro"
    }

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    /** CA: email válido y ≤ 40 caracteres. */
    fun emailValido(email: String): Boolean {
        val limpio = email.trim()
        return limpio.length <= MAX_EMAIL_LENGTH && EMAIL_REGEX.matches(limpio)
    }

    /** CA: contraseña solo dígitos, entre 6 y 10 caracteres. */
    fun passwordValida(password: String): Boolean {
        return password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH &&
            password.all { it.isDigit() }
    }

    fun formularioValido(email: String, password: String): Boolean =
        emailValido(email) && passwordValida(password)

    fun login(email: String, password: String) = ejecutar(ERROR_LOGIN) {
        repository.login(email.trim(), password)
    }

    fun registrar(email: String, password: String) = ejecutar(ERROR_REGISTRO) {
        repository.registrar(email.trim(), password)
    }

    fun consumirEstado() {
        _uiState.value = AuthUiState.Idle
    }

    private fun ejecutar(mensajeError: String, bloque: suspend () -> Unit) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                bloque()
                _uiState.value = AuthUiState.Success
            } catch (e: Exception) {
                Timber.e(e, "Error de autenticación")
                _uiState.value = AuthUiState.Error(mensajeError)
            }
        }
    }
}
