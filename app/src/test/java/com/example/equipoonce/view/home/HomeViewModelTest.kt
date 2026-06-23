package com.example.equipoonce.view.home

import com.example.equipoonce.repository.AuthRepository
import com.example.equipoonce.test.MainDispatcherRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
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

    // ── Giro de la botella (HU 12) ───────────────────────────────────────────

    @Test
    fun `onPresionameClicked oculta el boton y emite el evento de giro`() {
        viewModel.onPresionameClicked()

        assertFalse(viewModel.isButtonVisible.value)
        assertNotNull(viewModel.spinEvent.value)
    }

    @Test
    fun `onSpinEventConsumed limpia el evento de giro`() {
        viewModel.onPresionameClicked()

        viewModel.onSpinEventConsumed()

        assertNull(viewModel.spinEvent.value)
    }

    @Test
    fun `tras el giro se muestra el dialogo y se reactiva el boton`() = runTest {
        viewModel.onPresionameClicked()
        advanceUntilIdle() // corre giro + cuenta regresiva (tiempo virtual)

        assertTrue(viewModel.showDialog.value)
        assertTrue(viewModel.isButtonVisible.value)
        assertNull(viewModel.contador.value)
    }

    @Test
    fun `onDialogShown limpia la senal de mostrar dialogo`() = runTest {
        viewModel.onPresionameClicked()
        advanceUntilIdle()

        viewModel.onDialogShown()

        assertFalse(viewModel.showDialog.value)
    }

    @Test
    fun `onDialogClosed deja el juego listo para una nueva partida`() {
        viewModel.onPresionameClicked()

        viewModel.onDialogClosed()

        assertFalse(viewModel.isCounting())
    }
}
