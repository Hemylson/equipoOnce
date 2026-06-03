package com.example.equipoonce.domain.usecase

import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.domain.repository.IRetoRepository
import javax.inject.Inject

class GetRandomRetoUseCase @Inject constructor(
    private val repository: IRetoRepository
) {
    suspend operator fun invoke(): RetoEntity? = repository.obtenerTodos().randomOrNull()
}
