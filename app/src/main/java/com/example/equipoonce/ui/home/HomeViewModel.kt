package com.example.equipoonce.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _contador = MutableLiveData<Int?>(null)
    val contador: LiveData<Int?> = _contador

    private val _isButtonVisible = MutableLiveData(true)
    val isButtonVisible: LiveData<Boolean> = _isButtonVisible

    private var counting = false

    fun onPresionameClicked() {
        if (counting) return
        counting = true
        _isButtonVisible.value = false
        viewModelScope.launch {
            for (value in 3 downTo 0) {
                _contador.value = value
                delay(1000)
            }
            _contador.value = null
            _isButtonVisible.value = true
            counting = false
        }
    }

    fun isCounting(): Boolean = counting
}
