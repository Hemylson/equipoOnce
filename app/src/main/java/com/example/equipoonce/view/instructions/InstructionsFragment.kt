package com.example.equipoonce.view.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.equipoonce.databinding.FragmentInstructionsBinding
import com.example.equipoonce.di.RepositoryProvider
import com.example.equipoonce.di.SharedAudioViewModelFactory
import com.example.equipoonce.view.shared.SharedAudioViewModel

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    // Accede al ViewModel de audio compartido (no al HomeViewModel)
    private val audioViewModel: SharedAudioViewModel by activityViewModels {
        SharedAudioViewModelFactory(RepositoryProvider.provideAudioManager())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarInstructions.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // Criterio 1 HU-5: pausa audio al entrar (cualquier método de navegación)
    override fun onResume() {
        super.onResume()
        audioViewModel.pauseForNavigation()
    }

    // Criterio 3b HU-5: reanuda audio al salir (cualquier método de navegación)
    override fun onPause() {
        super.onPause()
        audioViewModel.resumeIfNeeded()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
