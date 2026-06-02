package com.example.equipoonce.data.remote.dto

import com.google.gson.annotations.SerializedName

// Wrapper del JSON: { "pokemon": [ ... ] }
data class PokedexResponse(
    @SerializedName("pokemon") val pokemon: List<PokemonDto>
)

data class PokemonDto(
    @SerializedName("id")   val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("img")  val img: String = ""   // URL directa de la imagen
)