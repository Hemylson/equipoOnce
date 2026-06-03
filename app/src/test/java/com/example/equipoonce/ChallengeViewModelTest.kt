package com.example.equipoonce

import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.domain.usecase.GetRandomPokemonUseCase
import com.example.equipoonce.domain.usecase.GetRandomRetoUseCase
import com.example.equipoonce.ui.challenge.ChallengeUiState
import com.example.equipoonce.ui.challenge.ChallengeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChallengeViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before fun setup() = Dispatchers.setMain(dispatcher)
    @After fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `sin retos emite Error`() = runTest {
        val retoRepo = FakeRetoRepository()
        val pokemonRepo = FakePokemonRepository()
        val viewModel = ChallengeViewModel(
            getRandomReto = GetRandomRetoUseCase(retoRepo),
            getRandomPokemon = GetRandomPokemonUseCase(pokemonRepo)
        )
        viewModel.cargarRetoYPokemon()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is ChallengeUiState.Error)
    }

    @Test
    fun `con retos emite Success`() = runTest {
        val retoRepo = FakeRetoRepository()
        retoRepo.insertar(RetoEntity(descripcion = "Reto de prueba"))
        val pokemonRepo = FakePokemonRepository()
        val viewModel = ChallengeViewModel(
            getRandomReto = GetRandomRetoUseCase(retoRepo),
            getRandomPokemon = GetRandomPokemonUseCase(pokemonRepo)
        )
        viewModel.cargarRetoYPokemon()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is ChallengeUiState.Success)
    }

    @Test
    fun `con retos pero sin pokemon emite Success con pokemon null`() = runTest {
        val retoRepo = FakeRetoRepository()
        retoRepo.insertar(RetoEntity(descripcion = "Reto"))
        val pokemonRepo = FakePokemonRepository(emptyList())
        val viewModel = ChallengeViewModel(
            getRandomReto = GetRandomRetoUseCase(retoRepo),
            getRandomPokemon = GetRandomPokemonUseCase(pokemonRepo)
        )
        viewModel.cargarRetoYPokemon()
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertTrue(state is ChallengeUiState.Success)
        assertTrue((state as ChallengeUiState.Success).pokemon == null)
    }
}
