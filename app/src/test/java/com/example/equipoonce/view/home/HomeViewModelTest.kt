package com.example.equipoonce.view.home

import com.example.equipoonce.repository.AuthRepository
import com.example.equipoonce.test.MainDispatcherRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val viewModel = HomeViewModel(authRepository)

    @Test
    fun `cerrarSesion llama a logout del repositorio`() {
        viewModel.cerrarSesion()

        verify { authRepository.logout() }
    }

    @Test
    fun `cerrarSesion emite el evento de logout`() {
        viewModel.cerrarSesion()

        assertTrue(viewModel.logoutEvent.value)
    }

    @Test
    fun `onLogoutHandled limpia el evento de logout`() {
        viewModel.cerrarSesion()

        viewModel.onLogoutHandled()

        assertFalse(viewModel.logoutEvent.value)
    }
}
