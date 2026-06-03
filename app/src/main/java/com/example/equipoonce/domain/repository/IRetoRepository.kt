package com.example.equipoonce.domain.repository

import com.example.equipoonce.data.local.RetoEntity

/**
 * Contrato de acceso a datos de retos.
 * Cualquier implementación (Room, memoria, red) debe cumplir este contrato.
 */
interface IRetoRepository {
    /** Retorna todos los retos almacenados, ordenados por id descendente (más recientes primero). */
    suspend fun obtenerTodos(): List<RetoEntity>
    /** Inserta un nuevo reto. */
    suspend fun insertar(reto: RetoEntity)
    /** Actualiza un reto existente. */
    suspend fun actualizar(reto: RetoEntity)
    /** Elimina un reto. */
    suspend fun eliminar(reto: RetoEntity)
}
