package com.example.equipoonce

import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.domain.repository.IRetoRepository

class FakeRetoRepository : IRetoRepository {
    private val retos = mutableListOf<RetoEntity>()
    private var nextId = 1

    override suspend fun obtenerTodos(): List<RetoEntity> = retos.toList()

    override suspend fun insertar(reto: RetoEntity) {
        retos.add(reto.copy(id = nextId++))
    }

    override suspend fun actualizar(reto: RetoEntity) {
        val index = retos.indexOfFirst { it.id == reto.id }
        if (index >= 0) retos[index] = reto
    }

    override suspend fun eliminar(reto: RetoEntity) {
        retos.removeAll { it.id == reto.id }
    }
}
