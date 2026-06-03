package com.example.equipoonce.ui.challenge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.data.remote.RetrofitClient
import com.example.equipoonce.data.remote.dto.PokemonDto
import com.example.equipoonce.data.repository.PokemonRepository
import com.example.equipoonce.data.repository.RetoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ChallengeUiState {
    object Loading : ChallengeUiState()
    data class Success(val reto: RetoEntity, val pokemon: PokemonDto?) : ChallengeUiState()
    data class Error(val message: String) : ChallengeUiState()
}

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {

    private val retoRepository = RetoRepository(application)
    private val pokemonRepository = PokemonRepository(RetrofitClient.pokemonApiService)

    private val _uiState = MutableStateFlow<ChallengeUiState>(ChallengeUiState.Loading)
    val uiState: StateFlow<ChallengeUiState> = _uiState

    fun cargarRetoYPokemon() {
        viewModelScope.launch {
            _uiState.value = ChallengeUiState.Loading
            try {
                val reto = obtenerRetoAleatorio()
                if (reto == null) {
                    _uiState.value = ChallengeUiState.Error("No hay retos disponibles. Agrega retos primero.")
                    return@launch
                }
                val pokemon = obtenerPokemonAleatorio()
                _uiState.value = ChallengeUiState.Success(reto, pokemon)
            } catch (e: Exception) {
                _uiState.value = ChallengeUiState.Error("Error inesperado. Intenta de nuevo.")
            }
        }
    }

    private suspend fun obtenerRetoAleatorio(): RetoEntity? =
        retoRepository.obtenerTodos().randomOrNull()

    private suspend fun obtenerPokemonAleatorio(): PokemonDto? =
        pokemonRepository.getPokemonAleatorio()
}
