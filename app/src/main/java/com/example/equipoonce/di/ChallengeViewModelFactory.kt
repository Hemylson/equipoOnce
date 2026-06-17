package com.example.equipoonce.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.equipoonce.repository.PokemonRepository
import com.example.equipoonce.repository.RetoRepository
import com.example.equipoonce.view.challenge.ChallengeViewModel

class ChallengeViewModelFactory(
    private val retoRepository: RetoRepository,
    private val pokemonRepository: PokemonRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChallengeViewModel::class.java)) {
            return ChallengeViewModel(retoRepository, pokemonRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
