package com.example.equipoonce.domain.exception

sealed class AppException(message: String) : Exception(message) {
    class NetworkException(message: String = "Error de conexión. Verifica tu internet.") : AppException(message)
    class DatabaseException(message: String = "Error en la base de datos.") : AppException(message)
    class EmptyResultException(message: String = "No hay datos disponibles.") : AppException(message)
}
