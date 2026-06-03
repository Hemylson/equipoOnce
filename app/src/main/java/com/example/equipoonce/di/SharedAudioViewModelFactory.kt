package com.example.equipoonce.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.equipoonce.ui.shared.SharedAudioViewModel
import com.example.equipoonce.utils.GameAudioManager

class SharedAudioViewModelFactory(
    private val audioManager: GameAudioManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedAudioViewModel::class.java)) {
            return SharedAudioViewModel(audioManager) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
