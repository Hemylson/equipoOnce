package com.example.equipoonce.data.local

import androidx.room.*

@Dao
interface RetoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(reto: RetoEntity)

    @Query("SELECT * FROM retos ORDER BY id DESC")
    suspend fun obtenerTodos(): List<RetoEntity>

    @Update
    suspend fun actualizar(reto: RetoEntity)

    @Delete
    suspend fun eliminar(reto: RetoEntity)
}
