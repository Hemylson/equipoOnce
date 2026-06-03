package com.example.equipoonce.data.repository

import com.example.equipoonce.data.remote.PokemonApiService
import com.example.equipoonce.data.remote.dto.PokemonDto

class PokemonRepository(private val api: PokemonApiService) {

    // Descarga el pokedex completo y devuelve uno aleatorio
    suspend fun getPokemonAleatorio(): PokemonDto? = try {
        api.getPokedex().pokemon.randomOrNull()
    } catch (e: Exception) {
        null  // Si no hay red, el diálogo igual muestra el reto con placeholder
    }
}