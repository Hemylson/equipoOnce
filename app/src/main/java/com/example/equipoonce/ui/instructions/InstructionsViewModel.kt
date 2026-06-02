package com.example.equipoonce.ui.instructions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InstructionsViewModel : ViewModel() {

    // Estado observable para la música (true = reproducir, false = pausar)
    private val _isMusicPlaying = MutableLiveData<Boolean>()
    val isMusicPlaying: LiveData<Boolean> get() = _isMusicPlaying

    init {
        // Al iniciar la pantalla, enviamos la orden de pausar la música
        _isMusicPlaying.value = false
    }

    // Método para cuando el usuario presiona volver
    fun onBackButtonClicked() {
        // Al salir de la pantalla, enviamos la orden de reanudar la música
        _isMusicPlaying.value = true
    }
}
