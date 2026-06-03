package com.example.equipoonce

import com.example.equipoonce.domain.usecase.GetRandomPokemonUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class PokemonUseCasesTest {

    @Test
    fun `retorna pokemon aleatorio cuando hay datos`() = runTest {
        val repo = FakePokemonRepository()
        val useCase = GetRandomPokemonUseCase(repo)
        assertNotNull(useCase())
    }

    @Test
    fun `retorna null cuando lista esta vacia`() = runTest {
        val repo = FakePokemonRepository(emptyList())
        val useCase = GetRandomPokemonUseCase(repo)
        assertNull(useCase())
    }

    @Test
    fun `retorna uno de los pokemons disponibles`() = runTest {
        val repo = FakePokemonRepository()
        val useCase = GetRandomPokemonUseCase(repo)
        val result = useCase()
        val nombres = listOf("Bulbasaur", "Charmander", "Squirtle")
        assertNotNull(result)
        assertTrue(result!!.name in nombres)
    }
}
