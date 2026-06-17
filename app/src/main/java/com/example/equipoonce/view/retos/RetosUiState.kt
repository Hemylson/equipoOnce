package com.example.equipoonce.view.retos

import com.example.equipoonce.model.RetoEntity

sealed class RetosUiState {
    object Loading : RetosUiState()
    object Empty : RetosUiState()
    data class Success(val retos: List<RetoEntity>) : RetosUiState()
}
