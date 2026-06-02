package com.example.equipoonce.data.repository

import android.content.Context
import com.example.equipoonce.data.local.AppDatabase
import com.example.equipoonce.data.local.RetoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetoRepository(context: Context) {

    private val dao = AppDatabase.getDatabase(context).retoDao()

    suspend fun insertar(reto: RetoEntity) {
        withContext(Dispatchers.IO) { dao.insertar(reto) }
    }

    suspend fun obtenerTodos(): List<RetoEntity> {
        return withContext(Dispatchers.IO) { dao.obtenerTodos() }
    }

    suspend fun actualizar(reto: RetoEntity) {
        withContext(Dispatchers.IO) { dao.actualizar(reto) }
    }

    suspend fun eliminar(reto: RetoEntity) {
        withContext(Dispatchers.IO) { dao.eliminar(reto) }
    }
}