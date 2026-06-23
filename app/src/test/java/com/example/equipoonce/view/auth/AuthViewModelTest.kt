package com.example.equipoonce.view.auth

import com.example.equipoonce.repository.AuthRepository
import com.example.equipoonce.test.MainDispatcherRule
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: AuthRepository = mockk()

    private fun crearViewModel() = AuthViewModel(repository)

    // ── Validaciones ─────────────────────────────────────────────────────────

    @Test
    fun `emailValido acepta correo bien formado y rechaza invalidos o largos`() {
        val viewModel = crearViewModel()

        assertTrue(viewModel.emailValido("juan@correo.com"))
        assertFalse(viewModel.emailValido("juan-sin-arroba"))
        // 41 caracteres antes del dominio -> supera el máximo de 40
        assertFalse(viewModel.emailValido("a".repeat(41) + "@correo.com"))
    }

    @Test
    fun `passwordValida exige solo digitos entre 6 y 10`() {
        val viewModel = crearViewModel()

        assertTrue(viewModel.passwordValida("123456"))
        assertFalse(viewModel.passwordValida("12345"))      // muy corta
        assertFalse(viewModel.passwordValida("12345678901")) // muy larga
        assertFalse(viewModel.passwordValida("12ab56"))      // tiene letras
    }

    // ── login / registro ─────────────────────────────────────────────────────

    @Test
    fun `login exitoso deja el estado en Success`() = runTest {
        coEvery { repository.login(any(), any()) } returns mockk<FirebaseUser>()

        val viewModel = crearViewModel()
        viewModel.login("juan@correo.com", "123456")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is AuthUiState.Success)
    }

    @Test
    fun `login con error deja el estado en Error`() = runTest {
        coEvery { repository.login(any(), any()) } throws RuntimeException("credential is incorrect")

        val viewModel = crearViewModel()
        viewModel.login("juan@correo.com", "123456")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is AuthUiState.Error)
    }
}
