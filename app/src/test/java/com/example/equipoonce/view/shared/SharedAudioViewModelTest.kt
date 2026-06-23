package com.example.equipoonce.view.shared

import com.example.equipoonce.utils.GameAudioManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SharedAudioViewModelTest {

    private val audioManager: GameAudioManager = mockk(relaxed = true)

    private fun crearViewModel() = SharedAudioViewModel(audioManager)

    @Test
    fun `toggleAudio alterna el estado y pausa o reanuda el fondo`() {
        val viewModel = crearViewModel()

        // Estado inicial encendido -> al alternar queda apagado y se pausa.
        viewModel.toggleAudio()
        assertFalse(viewModel.isAudioOn.value)
        verify { audioManager.pauseBackground() }

        // Apagado -> encendido nuevamente.
        viewModel.toggleAudio()
        assertTrue(viewModel.isAudioOn.value)
    }

    @Test
    fun `startBackground reproduce la musica cuando no esta silenciado`() {
        val viewModel = crearViewModel()

        viewModel.startBackground()

        verify { audioManager.playBackground() }
    }

    @Test
    fun `onSpinStart pausa el fondo y reproduce el sonido del giro`() {
        every { audioManager.isBackgroundPlaying() } returns true
        val viewModel = crearViewModel()

        viewModel.onSpinStart()

        verify { audioManager.pauseBackground() }
        verify { audioManager.playSpinSound() }
    }

    @Test
    fun `onSpinEnd detiene el sonido del giro`() {
        val viewModel = crearViewModel()

        viewModel.onSpinEnd()

        verify { audioManager.stopSpinSound() }
    }

    @Test
    fun `onSpinDialogClosed reanuda el fondo si estaba sonando antes del giro`() {
        every { audioManager.isBackgroundPlaying() } returns true
        val viewModel = crearViewModel()

        viewModel.onSpinStart()       // captura que el fondo estaba sonando
        viewModel.onSpinDialogClosed()

        verify { audioManager.resumeBackground() }
    }
}
