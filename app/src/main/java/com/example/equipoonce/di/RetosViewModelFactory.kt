package com.example.equipoonce.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.equipoonce.repository.RetoRepository
import com.example.equipoonce.view.retos.RetosViewModel

class RetosViewModelFactory(
    private val repository: RetoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RetosViewModel::class.java)) {
            return RetosViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
