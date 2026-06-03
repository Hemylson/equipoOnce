package com.example.equipoonce.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _navegarAlHome = MutableLiveData(false)
    val navegarAlHome: LiveData<Boolean> = _navegarAlHome

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
