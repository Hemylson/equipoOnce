package com.example.equipoonce

import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.domain.usecase.AddRetoUseCase
import com.example.equipoonce.domain.usecase.DeleteRetoUseCase
import com.example.equipoonce.domain.usecase.EditRetoUseCase
import com.example.equipoonce.domain.usecase.GetAllRetosUseCase
import com.example.equipoonce.domain.usecase.GetRandomRetoUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RetosUseCasesTest {

    private lateinit var repo: FakeRetoRepository
    private lateinit var getAllRetos: GetAllRetosUseCase
    private lateinit var addReto: AddRetoUseCase
    private lateinit var editReto: EditRetoUseCase
    private lateinit var deleteReto: DeleteRetoUseCase
    private lateinit var getRandomReto: GetRandomRetoUseCase

    @Before
    fun setup() {
        repo = FakeRetoRepository()
        getAllRetos = GetAllRetosUseCase(repo)
        addReto = AddRetoUseCase(repo)
        editReto = EditRetoUseCase(repo)
        deleteReto = DeleteRetoUseCase(repo)
        getRandomReto = GetRandomRetoUseCase(repo)
    }

    @Test
    fun `lista vacia al inicio`() = runTest {
        val result = getAllRetos()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `agregar reto aumenta la lista`() = runTest {
        addReto("Cantar una canción")
        val result = getAllRetos()
        assertEquals(1, result.size)
        assertEquals("Cantar una canción", result.first().descripcion)
    }

    @Test
    fun `agregar multiples retos`() = runTest {
        addReto("Reto 1")
        addReto("Reto 2")
        addReto("Reto 3")
        assertEquals(3, getAllRetos().size)
    }

    @Test
    fun `editar reto actualiza descripcion`() = runTest {
        addReto("Original")
        val reto = getAllRetos().first()
        editReto(reto.copy(descripcion = "Modificado"))
        assertEquals("Modificado", getAllRetos().first().descripcion)
    }

    @Test
    fun `eliminar reto reduce la lista`() = runTest {
        addReto("Para eliminar")
        val reto = getAllRetos().first()
        deleteReto(reto)
        assertTrue(getAllRetos().isEmpty())
    }

    @Test
    fun `reto aleatorio es null cuando lista vacia`() = runTest {
        assertNull(getRandomReto())
    }

    @Test
    fun `reto aleatorio retorna un reto cuando hay datos`() = runTest {
        addReto("Reto existente")
        assertNotNull(getRandomReto())
    }
}
