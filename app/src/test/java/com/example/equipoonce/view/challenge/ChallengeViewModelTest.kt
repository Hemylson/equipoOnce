package com.example.equipoonce.view.challenge

import com.example.equipoonce.model.Reto
import com.example.equipoonce.repository.PokemonRepository
import com.example.equipoonce.repository.RetoRepository
import com.example.equipoonce.test.MainDispatcherRule
import com.example.equipoonce.webservice.dto.PokemonDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChallengeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val retoRepository: RetoRepository = mockk()
    private val pokemonRepository: PokemonRepository = mockk()

    private fun crearViewModel() = ChallengeViewModel(retoRepository, pokemonRepository)

    @Test
    fun `cargarRetoYPokemon combina reto y pokemon en estado Success`() = runTest {
        coEvery { retoRepository.obtenerTodos() } returns listOf(Reto("1", "Baila"))
        coEvery { pokemonRepository.getPokemonAleatorio() } returns PokemonDto(25, "Pikachu", "http://img")

        val viewModel = crearViewModel()
        viewModel.cargarRetoYPokemon()
        advanceUntilIdle()

        val estado = viewModel.uiState.value
        assertTrue(estado is ChallengeUiState.Success)
        estado as ChallengeUiState.Success
        assertEquals("Baila", estado.reto.descripcion)
        assertEquals("http://img", estado.pokemon?.img)
    }

    @Test
    fun `cargarRetoYPokemon sin retos emite estado Error`() = runTest {
        coEvery { retoRepository.obtenerTodos() } returns emptyList()
        coEvery { pokemonRepository.getPokemonAleatorio() } returns null

        val viewModel = crearViewModel()
        viewModel.cargarRetoYPokemon()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is ChallengeUiState.Error)
    }
}
