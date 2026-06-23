package com.example.equipoonce.view.retos

import com.example.equipoonce.model.Reto

sealed class RetosUiState {
    object Loading : RetosUiState()
    object Empty : RetosUiState()
    data class Success(val retos: List<Reto>) : RetosUiState()
    data class Error(val message: String) : RetosUiState()
}
