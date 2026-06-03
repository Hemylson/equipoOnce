package com.example.equipoonce.data.repository

import com.example.equipoonce.data.remote.PokemonApiService
import com.example.equipoonce.data.remote.dto.PokemonDto
import com.example.equipoonce.domain.exception.AppException
import com.example.equipoonce.domain.repository.IPokemonRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(
    private val api: PokemonApiService
) : IPokemonRepository {

    private var cache: List<PokemonDto>? = null

    override suspend fun obtenerTodos(): List<PokemonDto> {
        return try {
            cache ?: api.getPokedex().pokemon.also {
                cache = it
                Timber.d("Pokémon cargados: ${it.size}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error al cargar Pokémon")
            throw AppException.NetworkException("No se pudo cargar la lista de Pokémon. Verifica tu conexión.")
        }
    }
}
