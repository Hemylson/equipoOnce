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

            // Reto aleatorio desde SQLite
            val todos = retoRepository.obtenerTodos()
            val reto = todos.randomOrNull()
            if (reto == null) {
                _uiState.value = ChallengeUiState.Error("No hay retos disponibles. Agrega retos primero.")
                return@launch
            }

            // Pokémon aleatorio desde Biuni (falla silenciosamente si no hay red)
            val pokemon = pokemonRepository.getPokemonAleatorio()

            _uiState.value = ChallengeUiState.Success(reto, pokemon)
        }
    }
}