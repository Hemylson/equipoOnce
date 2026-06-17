package com.example.equipoonce.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "retos")
data class RetoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val descripcion: String
)
