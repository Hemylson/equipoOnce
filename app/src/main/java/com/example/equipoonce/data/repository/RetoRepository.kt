package com.example.equipoonce.data.repository

import com.example.equipoonce.data.local.RetoDao
import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.domain.exception.AppException
import com.example.equipoonce.domain.repository.IRetoRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetoRepository @Inject constructor(
    private val dao: RetoDao
) : IRetoRepository {

    override suspend fun obtenerTodos(): List<RetoEntity> = try {
        dao.obtenerTodos().also { Timber.d("Retos obtenidos: ${it.size}") }
    } catch (e: Exception) {
        Timber.e(e, "Error al obtener retos")
        throw AppException.DatabaseException("No se pudieron cargar los retos.")
    }

    override suspend fun insertar(reto: RetoEntity) = try {
        dao.insertar(reto).also { Timber.d("Reto insertado: ${reto.descripcion}") }
    } catch (e: Exception) {
        Timber.e(e, "Error al insertar reto")
        throw AppException.DatabaseException("No se pudo guardar el reto.")
    }

    override suspend fun actualizar(reto: RetoEntity) = try {
        dao.actualizar(reto).also { Timber.d("Reto actualizado: ${reto.id}") }
    } catch (e: Exception) {
        Timber.e(e, "Error al actualizar reto")
        throw AppException.DatabaseException("No se pudo actualizar el reto.")
    }

    override suspend fun eliminar(reto: RetoEntity) = try {
        dao.eliminar(reto).also { Timber.d("Reto eliminado: ${reto.id}") }
    } catch (e: Exception) {
        Timber.e(e, "Error al eliminar reto")
        throw AppException.DatabaseException("No se pudo eliminar el reto.")
    }
}
