package com.example.equipoonce.ui.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.data.remote.dto.PokemonDto
import com.example.equipoonce.domain.usecase.GetRandomPokemonUseCase
import com.example.equipoonce.domain.usecase.GetRandomRetoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ChallengeUiState {
    object Loading : ChallengeUiState()
    data class Success(val reto: RetoEntity, val pokemon: PokemonDto?) : ChallengeUiState()
    data class Error(val message: String) : ChallengeUiState()
}

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val getRandomReto: GetRandomRetoUseCase,
    private val getRandomPokemon: GetRandomPokemonUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChallengeUiState>(ChallengeUiState.Loading)
    val uiState: StateFlow<ChallengeUiState> = _uiState

    fun cargarRetoYPokemon() {
        viewModelScope.launch {
            _uiState.value = ChallengeUiState.Loading
            try {
                val reto = getRandomReto()
                if (reto == null) {
                    _uiState.value = ChallengeUiState.Error("No hay retos disponibles. Agrega retos primero.")
                    return@launch
                }
                val pokemon = getRandomPokemon()
                _uiState.value = ChallengeUiState.Success(reto, pokemon)
            } catch (e: Exception) {
                _uiState.value = ChallengeUiState.Error("Error inesperado. Intenta de nuevo.")
            }
        }
    }
}
