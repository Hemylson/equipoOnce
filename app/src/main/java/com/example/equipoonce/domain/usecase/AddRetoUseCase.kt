package com.example.equipoonce.domain.usecase

import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.domain.exception.AppException
import com.example.equipoonce.domain.repository.IRetoRepository
import javax.inject.Inject

class AddRetoUseCase @Inject constructor(
    private val repository: IRetoRepository
) {
    companion object {
        const val MIN_LENGTH = 3
        const val MAX_LENGTH = 200
    }

    suspend operator fun invoke(descripcion: String) {
        val trimmed = descripcion.trim()
        when {
            trimmed.isBlank() -> throw AppException.EmptyResultException("El reto no puede estar vacío.")
            trimmed.length < MIN_LENGTH -> throw AppException.EmptyResultException("El reto debe tener al menos $MIN_LENGTH caracteres.")
            trimmed.length > MAX_LENGTH -> throw AppException.EmptyResultException("El reto no puede superar $MAX_LENGTH caracteres.")
        }
        repository.insertar(RetoEntity(descripcion = trimmed))
    }
}
