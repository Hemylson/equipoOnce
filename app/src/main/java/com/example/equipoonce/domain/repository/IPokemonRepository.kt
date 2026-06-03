package com.example.equipoonce.domain.repository

import com.example.equipoonce.data.remote.dto.PokemonDto

/**
 * Contrato de acceso a datos de Pokémon.
 * La implementación puede obtenerlos de red, caché o cualquier fuente.
 */
interface IPokemonRepository {
    /** Retorna la lista completa de Pokémon disponibles. */
    suspend fun obtenerTodos(): List<PokemonDto>
}
