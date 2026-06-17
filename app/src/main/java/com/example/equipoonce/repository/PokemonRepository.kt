package com.example.equipoonce.repository

import com.example.equipoonce.webservice.PokemonApiService
import com.example.equipoonce.model.PokemonDto

class PokemonRepository(private val api: PokemonApiService) {

    private var cache: List<PokemonDto>? = null

    suspend fun getPokemonAleatorio(): PokemonDto? = try {
        val lista = cache ?: api.getPokedex().pokemon.also { cache = it }
        lista.randomOrNull()
    } catch (e: Exception) {
        null
    }
}