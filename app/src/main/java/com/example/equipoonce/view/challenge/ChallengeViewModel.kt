package com.example.equipoonce.view.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.model.Reto
import com.example.equipoonce.webservice.dto.PokemonDto
import com.example.equipoonce.repository.PokemonRepository
import com.example.equipoonce.repository.RetoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed class ChallengeUiState {
    object Loading : ChallengeUiState()
    data class Success(val reto: Reto, val pokemon: PokemonDto?) : ChallengeUiState()
    data class Error(val message: String) : ChallengeUiState()
}

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val retoRepository: RetoRepository,
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChallengeUiState>(ChallengeUiState.Loading)
    val uiState: StateFlow<ChallengeUiState> = _uiState

    private val _isLoading = MutableStateFlow(false)

    fun cargarRetoYPokemon() {
        if (_isLoading.value) {
            Timber.d("Carga ignorada — ya hay una en curso")
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = ChallengeUiState.Loading
            Timber.d("Iniciando carga de reto y Pokémon")
            try {
                val reto = obtenerRetoAleatorio()
                if (reto == null) {
                    Timber.w("No hay retos disponibles en la BD")
                    _uiState.value = ChallengeUiState.Error("No hay retos disponibles. Agrega retos primero.")
                    return@launch
                }
                val pokemon = obtenerPokemonAleatorio()
                Timber.d("Carga completada — reto: ${reto.descripcion}, pokemon: ${pokemon?.name}")
                _uiState.value = ChallengeUiState.Success(reto, pokemon)
            } catch (e: Exception) {
                Timber.e(e, "Error al cargar reto y Pokémon")
                _uiState.value = ChallengeUiState.Error("Error inesperado. Intenta de nuevo.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun obtenerRetoAleatorio(): Reto? =
        retoRepository.obtenerTodos().randomOrNull()

    private suspend fun obtenerPokemonAleatorio(): PokemonDto? =
        pokemonRepository.getPokemonAleatorio()
}
