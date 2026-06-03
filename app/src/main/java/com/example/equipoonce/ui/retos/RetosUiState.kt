package com.example.equipoonce.ui.retos

import com.example.equipoonce.data.local.RetoEntity

sealed class RetosUiState {
    object Loading : RetosUiState()
    object Empty : RetosUiState()
    data class Success(val retos: List<RetoEntity>) : RetosUiState()
    data class Error(val message: String) : RetosUiState()
}
