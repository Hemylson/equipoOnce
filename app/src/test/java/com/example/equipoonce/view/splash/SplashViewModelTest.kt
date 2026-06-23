package com.example.equipoonce.view.splash

import com.example.equipoonce.test.MainDispatcherRule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val auth: FirebaseAuth = mockk()

    private fun crearViewModel() = SplashViewModel(auth)

    @Test
    fun `con sesion activa el splash navega al Home`() = runTest {
        every { auth.currentUser } returns mockk<FirebaseUser>()

        val viewModel = crearViewModel()
        viewModel.iniciarSplash()
        advanceUntilIdle() // salta los 5 s de delay (tiempo virtual)

        assertTrue(viewModel.destination.value is SplashDestination.Home)
    }

    @Test
    fun `sin sesion el splash navega al Login`() = runTest {
        every { auth.currentUser } returns null

        val viewModel = crearViewModel()
        viewModel.iniciarSplash()
        advanceUntilIdle()

        assertTrue(viewModel.destination.value is SplashDestination.Login)
    }
}
