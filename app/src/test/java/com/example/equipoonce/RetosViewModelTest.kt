package com.example.equipoonce

import com.example.equipoonce.domain.usecase.AddRetoUseCase
import com.example.equipoonce.domain.usecase.DeleteRetoUseCase
import com.example.equipoonce.domain.usecase.EditRetoUseCase
import com.example.equipoonce.domain.usecase.GetAllRetosUseCase
import com.example.equipoonce.ui.retos.RetosUiState
import com.example.equipoonce.ui.retos.RetosViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RetosViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var repo: FakeRetoRepository
    private lateinit var viewModel: RetosViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repo = FakeRetoRepository()
        viewModel = RetosViewModel(
            getAllRetos = GetAllRetosUseCase(repo),
            addReto = AddRetoUseCase(repo),
            editReto = EditRetoUseCase(repo),
            deleteReto = DeleteRetoUseCase(repo)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `estado inicial es Loading`() {
        assertTrue(viewModel.uiState.value is RetosUiState.Loading)
    }

    @Test
    fun `cargarRetos con lista vacia emite Empty`() = runTest {
        viewModel.cargarRetos()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is RetosUiState.Empty)
    }

    @Test
    fun `cargarRetos con datos emite Success`() = runTest {
        repo.insertar(com.example.equipoonce.data.local.RetoEntity(descripcion = "Test reto"))
        viewModel.cargarRetos()
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertTrue(state is RetosUiState.Success)
        assertEquals(1, (state as RetosUiState.Success).retos.size)
    }

    @Test
    fun `agregarReto actualiza lista`() = runTest {
        viewModel.agregarReto("Reto nuevo")
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertTrue(state is RetosUiState.Success)
        assertEquals(1, (state as RetosUiState.Success).retos.size)
    }

    @Test
    fun `agregarReto con descripcion muy corta emite Error`() = runTest {
        viewModel.agregarReto("ab")
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is RetosUiState.Error)
    }

    @Test
    fun `agregarReto con descripcion vacia emite Error`() = runTest {
        viewModel.agregarReto("   ")
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is RetosUiState.Error)
    }

    @Test
    fun `eliminarReto reduce la lista`() = runTest {
        viewModel.agregarReto("Para eliminar")
        advanceUntilIdle()
        val reto = (viewModel.uiState.value as RetosUiState.Success).retos.first()
        viewModel.eliminarReto(reto)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is RetosUiState.Empty)
    }
}
