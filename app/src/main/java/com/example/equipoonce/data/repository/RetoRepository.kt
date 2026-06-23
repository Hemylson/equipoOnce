package com.example.equipoonce.data.repository

import com.example.equipoonce.data.local.RetoDao
import com.example.equipoonce.data.local.RetoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio de retos. Recibe el DAO por constructor (inyección de dependencias manual).
 * No depende de Context — es un ViewModel puro y testeable.
 */
class RetoRepository(private val dao: RetoDao) {

    suspend fun insertar(reto: RetoEntity) =
        withContext(Dispatchers.IO) { dao.insertar(reto) }

    suspend fun obtenerTodos(): List<RetoEntity> =
        withContext(Dispatchers.IO) { dao.obtenerTodos() }

    suspend fun actualizar(reto: RetoEntity) =
        withContext(Dispatchers.IO) { dao.actualizar(reto) }

    suspend fun eliminar(reto: RetoEntity) =
        withContext(Dispatchers.IO) { dao.eliminar(reto) }
}
