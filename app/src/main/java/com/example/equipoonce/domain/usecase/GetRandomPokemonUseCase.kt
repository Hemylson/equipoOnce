package com.example.equipoonce.domain.usecase

import com.example.equipoonce.data.remote.dto.PokemonDto
import com.example.equipoonce.domain.repository.IPokemonRepository
import javax.inject.Inject

class GetRandomPokemonUseCase @Inject constructor(
    private val repository: IPokemonRepository
) {
    suspend operator fun invoke(): PokemonDto? = repository.obtenerTodos().randomOrNull()
}
