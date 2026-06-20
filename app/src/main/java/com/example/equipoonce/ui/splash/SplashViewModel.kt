package com.example.equipoonce.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/** Destino al que debe navegar el splash luego de los 5 segundos */
sealed class SplashDestination {
    object Login : SplashDestination()
    object Home : SplashDestination()
}

class SplashViewModel : ViewModel() {

    companion object {
        private const val SPLASH_DURATION_MS = 5000L
    }

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination

    /**
     * CA-1: Espera 5 segundos.
     * CA-2: Si hay sesión activa en Firebase, navega a Home; si no, a Login.
     *
     * TODO (Firebase): cuando el equipo integre Firebase Auth, reemplazar
     * la línea `val hayUsuarioActivo = false` por:
     *
     *   val hayUsuarioActivo = FirebaseAuth.getInstance().currentUser != null
     *
     * y agregar el import:
     *   import com.google.firebase.auth.FirebaseAuth
     */
    fun iniciarSplash() {
        viewModelScope.launch {
            delay(SPLASH_DURATION_MS)

            // TODO (Firebase): reemplazar por FirebaseAuth.getInstance().currentUser != null
            val hayUsuarioActivo = false

            _destination.value = if (hayUsuarioActivo) {
                SplashDestination.Home
            } else {
                SplashDestination.Login
            }
        }
    }
}