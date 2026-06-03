package com.example.equipoonce.domain.usecase

import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.domain.repository.IRetoRepository
import javax.inject.Inject

class GetAllRetosUseCase @Inject constructor(
    private val repository: IRetoRepository
) {
    suspend operator fun invoke(): List<RetoEntity> = repository.obtenerTodos()
}
