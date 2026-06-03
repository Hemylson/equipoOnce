package com.example.equipoonce.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class SpinParams(val targetAngle: Float, val durationMs: Long)

class HomeViewModel : ViewModel() {

    private val _contador = MutableLiveData<Int?>(null)
    val contador: LiveData<Int?> = _contador

    private val _isButtonVisible = MutableLiveData(true)
    val isButtonVisible: LiveData<Boolean> = _isButtonVisible

    private val _isSpinning = MutableLiveData(false)
    val isSpinning: LiveData<Boolean> = _isSpinning

    private val _spinEvent = MutableLiveData<SpinParams?>(null)
    val spinEvent: LiveData<SpinParams?> = _spinEvent

    private val _showDialog = MutableLiveData(false)
    val showDialog: LiveData<Boolean> = _showDialog

    private var counting = false
    private var currentRotation = 0f

    fun onPresionameClicked() {
        if (counting) return

        counting = true
        _isButtonVisible.value = false
        _isSpinning.value = true
        _contador.value = null

        val durationMs = (3000..5000).random().toLong()
        val direction = if (Random.nextBoolean()) 1 else -1
        val fullSpins = (3..5).random()
        val extraAngle = Random.nextInt(0, 360)
        val targetAngle = currentRotation + direction * (fullSpins * 360 + extraAngle)

        _spinEvent.value = SpinParams(targetAngle, durationMs)

        viewModelScope.launch {
            delay(durationMs)
            currentRotation = ((targetAngle % 360f) + 360f) % 360f
            _isSpinning.value = false

            for (value in 3 downTo 0) {
                _contador.value = value
                delay(1000)
            }

            _contador.value = null
            _showDialog.value = true
        }
    }

    fun clearSpinEvent() {
        _spinEvent.value = null
    }

    fun onDialogShown() {
        _showDialog.value = false
    }

    fun onDialogClosed() {
        _isButtonVisible.value = true
        counting = false
    }

    fun isCounting(): Boolean = counting
}
