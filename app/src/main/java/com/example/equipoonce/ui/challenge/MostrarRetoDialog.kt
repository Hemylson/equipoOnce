package com.example.equipoonce.ui.challenge

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.CircleCropTransformation
import com.example.equipoonce.R
import com.example.equipoonce.databinding.DialogMostrarRetoBinding
import kotlinx.coroutines.launch

class MostrarRetoDialog : DialogFragment() {

    private var _binding: DialogMostrarRetoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChallengeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogMostrarRetoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fondo transparente para ver el degradado del XML
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // CA-6: Solo se cierra con el botón
        isCancelable = false

        // CA-5: Cerrar el diálogo
        binding.btnCerrar.setOnClickListener { dismiss() }

        observarEstado()
        viewModel.cargarRetoYPokemon()
    }

    private fun observarEstado() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ChallengeUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.tvReto.visibility = View.GONE
                            binding.imgPokemon.visibility = View.GONE
                        }
                        is ChallengeUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvReto.visibility = View.VISIBLE
                            binding.imgPokemon.visibility = View.VISIBLE

                            binding.tvReto.text = state.reto.descripcion

                            // CA-2: img es la URL directa del campo "img" del JSON de Biuni
                            binding.imgPokemon.load(state.pokemon?.img) {
                                crossfade(true)
                                transformations(CircleCropTransformation())
                                placeholder(R.drawable.ic_pokemon_placeholder)
                                error(R.drawable.ic_pokemon_placeholder)
                            }
                        }
                        is ChallengeUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvReto.visibility = View.VISIBLE
                            binding.imgPokemon.visibility = View.VISIBLE
                            binding.tvReto.text = state.message
                            binding.imgPokemon.setImageResource(R.drawable.ic_pokemon_placeholder)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "MostrarRetoDialog"
        fun newInstance() = MostrarRetoDialog()
    }
}