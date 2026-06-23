package com.example.equipoonce.view.retos

import com.example.equipoonce.model.Reto
import com.example.equipoonce.repository.RetoRepository
import com.example.equipoonce.test.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RetosViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: RetoRepository = mockk(relaxed = true)

    private fun crearViewModel() = RetosViewModel(repository)

    @Test
    fun `cargarRetos con datos expone Success con la lista`() = runTest {
        coEvery { repository.obtenerTodos() } returns listOf(Reto("1", "Baila"))

        val viewModel = crearViewModel()
        viewModel.cargarRetos()
        advanceUntilIdle()

        val estado = viewModel.uiState.value
        assertTrue(estado is RetosUiState.Success)
        assertEquals("Baila", (estado as RetosUiState.Success).retos.first().descripcion)
    }

    @Test
    fun `cargarRetos sin datos expone Empty`() = runTest {
        coEvery { repository.obtenerTodos() } returns emptyList()

        val viewModel = crearViewModel()
        viewModel.cargarRetos()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is RetosUiState.Empty)
    }

    @Test
    fun `agregarReto valido inserta en el repositorio`() = runTest {
        coEvery { repository.obtenerTodos() } returns emptyList()

        val viewModel = crearViewModel()
        viewModel.agregarReto("Cantar una canción")
        advanceUntilIdle()

        coVerify { repository.insertar(Reto(descripcion = "Cantar una canción")) }
    }

    @Test
    fun `agregarReto vacio emite Error y no inserta`() = runTest {
        val viewModel = crearViewModel()
        viewModel.agregarReto("   ")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is RetosUiState.Error)
        coVerify(exactly = 0) { repository.insertar(any()) }
    }
}
