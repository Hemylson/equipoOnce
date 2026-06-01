package com.example.equipoonce.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeViewModel : ViewModel() {

    private val _rotationAngle = MutableLiveData(0f)
    val rotationAngle: LiveData<Float> = _rotationAngle

    private val _spinDuration = MutableLiveData(4000L)
    val spinDuration: LiveData<Long> = _spinDuration

    private val _isSpinning = MutableLiveData(false)
    val isSpinning: LiveData<Boolean> = _isSpinning

    private val _contador = MutableLiveData<Int?>(null)
    val contador: LiveData<Int?> = _contador

    private val _showRetoDialogEvent = MutableLiveData<Unit>()
    val showRetoDialogEvent: LiveData<Unit> = _showRetoDialogEvent

    private var anguloAcumulado = 0f

    fun girarBotella() {
        if (_isSpinning.value == true) return

        _isSpinning.value = true
        _contador.value = null
        _spinDuration.value = Random.nextLong(3000L, 5001L)

        val anguloExtra = Random.nextInt(720, 1801).toFloat()
        anguloAcumulado += anguloExtra
        _rotationAngle.value = anguloAcumulado

        viewModelScope.launch {
            delay(_spinDuration.value ?: 4000L)
            iniciarContador()
        }
    }

    private suspend fun iniciarContador() {
        for (valor in 3 downTo 0) {
            _contador.value = valor
            delay(1000L)
        }
        _showRetoDialogEvent.value = Unit
    }

    fun onRetoDialogClosed() {
        _contador.value = null
        _isSpinning.value = false
    }
}
