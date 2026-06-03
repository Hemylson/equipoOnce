package com.example.equipoonce.data.repository

import com.example.equipoonce.data.remote.PokemonApiService
import com.example.equipoonce.data.remote.dto.PokemonDto

class PokemonRepository(private val api: PokemonApiService) {

    private var cache: List<PokemonDto>? = null

    suspend fun getPokemonAleatorio(): PokemonDto? = try {
        val lista = cache ?: api.getPokedex().pokemon.also { cache = it }
        lista.randomOrNull()
    } catch (e: Exception) {
        null
    }
}