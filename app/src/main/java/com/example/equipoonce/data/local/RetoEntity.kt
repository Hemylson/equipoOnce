package com.example.equipoonce.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "retos")
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "retos")
data class RetoEntity(
    @PrimaryKey
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val descripcion: String
)
