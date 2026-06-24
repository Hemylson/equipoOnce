package com.example.equipoonce.model

data class Reto(
    val id: String = "",
    val descripcion: String = "",
    // HU 7.0 C6: marca de creación para listar el reto más reciente arriba.
    val timestamp: Long = System.currentTimeMillis()
)
