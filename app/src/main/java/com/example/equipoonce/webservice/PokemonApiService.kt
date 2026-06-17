package com.example.equipoonce.webservice

import com.example.equipoonce.model.PokedexResponse
import retrofit2.http.GET

interface PokemonApiService {
    // HU-12: consume el pokedex completo de Biuni para obtener imagen aleatoria
    @GET("pokedex.json")
    suspend fun getPokedex(): PokedexResponse
}