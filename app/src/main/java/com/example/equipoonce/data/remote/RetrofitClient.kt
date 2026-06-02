package com.example.equipoonce.data.remote

import com.example.equipoonce.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val pokemonApiService: PokemonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.POKEMON_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApiService::class.java)
    }
}
