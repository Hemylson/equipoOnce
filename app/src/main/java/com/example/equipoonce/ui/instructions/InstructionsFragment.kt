package com.example.equipoonce.ui.instructions

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.equipoonce.databinding.FragmentInstructionsBinding

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    // Instanciación del ViewModel bajo el patrón MVVM
    private val viewModel: InstructionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configurar Observadores de estados (MVVM)
        setupObservers()

        // 2. Criterio de Aceptación: Iniciar la animación del recurso SVG
        val imageView = binding.imgTriunfo
        val drawable = imageView.drawable
        if (drawable is Animatable) {
            drawable.start()
        }

        // 3. Criterio de Aceptación: Evento de la flecha de regreso en la Toolbar
        binding.toolbarInstructions.setNavigationOnClickListener {
            viewModel.onBackButtonClicked()
            // Cerramos el fragmento y regresamos a la pantalla de inicio (Home)
            parentFragmentManager.popBackStack()
        }
    }

    /**
     * El Fragment reacciona de manera pasiva a lo que ordene el ViewModel
     */
    private fun setupObservers() {
        viewModel.isMusicPlaying.observe(viewLifecycleOwner) { shouldPlay ->
            // Aquí la interfaz reaccionará cuando el ViewModel cambie el estado de reproducción.
            // Toda la lógica interna del audio correrá de forma segura desde el ViewModel
            // usando el GameAudioManager de tu equipo.
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
