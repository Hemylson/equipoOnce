package com.example.equipoonce.view.challenge

import android.graphics.Color
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.CircleCropTransformation
import com.example.equipoonce.R
import com.example.equipoonce.databinding.DialogMostrarRetoBinding
import kotlinx.coroutines.launch
import timber.log.Timber
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * DIALOG_WIDTH_PERCENT).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
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
                            binding.tvReto.visibility = View.VISIBLE
                            binding.divider.visibility = View.VISIBLE
                            binding.tvReto.text = state.reto.descripcion

                            // Carga imagen: progressBar se mantiene hasta que Coil termine
                            binding.imgPokemon.load(state.pokemon?.img) {
                                crossfade(false)
                                transformations(CircleCropTransformation())
                                error(R.drawable.ic_pokemon_placeholder)
                                listener(
                                    onSuccess = { _, _ ->
                                        binding.progressBar.visibility = View.GONE
                                        binding.imgPokemon.visibility = View.VISIBLE
                                    },
                                    onError = { _, result ->
                                        Timber.e("Error al cargar imagen del Pokémon: ${result.throwable?.message}")
                                        binding.progressBar.visibility = View.GONE
                                        binding.imgPokemon.visibility = View.VISIBLE
                                    }
                                )
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

    override fun onDismiss(dialog: DialogInterface) {
        setFragmentResult(RESULT_KEY, bundleOf(KEY_DIALOG_CLOSED to true))
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "MostrarRetoDialog"
        const val RESULT_KEY = "MostrarRetoDialogResult"
        const val KEY_DIALOG_CLOSED = "closed"
        private const val DIALOG_WIDTH_PERCENT = 0.90
        fun newInstance() = MostrarRetoDialog()
    }
}
