package com.example.equipoonce

import com.example.equipoonce.data.remote.dto.PokemonDto
import com.example.equipoonce.domain.repository.IPokemonRepository

class FakePokemonRepository(
    private val pokemons: List<PokemonDto> = listOf(
        PokemonDto(1, "Bulbasaur", "http://img/1.png"),
        PokemonDto(2, "Charmander", "http://img/2.png"),
        PokemonDto(3, "Squirtle", "http://img/3.png")
    )
) : IPokemonRepository {
    override suspend fun obtenerTodos(): List<PokemonDto> = pokemons
}
