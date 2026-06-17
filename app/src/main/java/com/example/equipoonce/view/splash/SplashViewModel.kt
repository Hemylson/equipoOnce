package com.example.equipoonce.view.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _navegarAlHome = MutableStateFlow(false)
    val navegarAlHome: StateFlow<Boolean> = _navegarAlHome

    init {
        viewModelScope.launch {
            delay(Constants.SPLASH_DELAY_MS)
            _navegarAlHome.value = true
        }
    }

    fun onNavegado() {
        _navegarAlHome.value = false
    }
}
