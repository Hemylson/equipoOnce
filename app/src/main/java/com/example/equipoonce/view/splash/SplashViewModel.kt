package com.example.equipoonce.view.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Destino al que debe navegar el splash luego de los 5 segundos */
sealed class SplashDestination {
    object Login : SplashDestination()
    object Home : SplashDestination()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    companion object {
        private const val SPLASH_DURATION_MS = 5000L
    }

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination

    /**
     * CA-1: Espera 5 segundos.
     * CA-2: Si hay sesión activa en Firebase, navega a Home; si no, a Login.
     */
    fun iniciarSplash() {
        viewModelScope.launch {
            delay(SPLASH_DURATION_MS)

            val hayUsuarioActivo = auth.currentUser != null

            _destination.value = if (hayUsuarioActivo) {
                SplashDestination.Home
            } else {
                SplashDestination.Login
            }
        }
    }
}