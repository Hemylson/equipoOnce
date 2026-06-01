package com.example.equipoonce.ui.challenge

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.equipoonce.databinding.DialogMostrarRetoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MostrarRetoDialog : DialogFragment() {

    private var _binding: DialogMostrarRetoBinding? = null
    private val binding get() = _binding!!

    // TODO: recibir RetoEntity via Bundle y cargar imagen de Pokémon con Coil

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogMostrarRetoBinding.inflate(layoutInflater)
        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setPositiveButton("Hecho", null)
            .setNegativeButton("Pasar", null)
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!parentFragmentManager.isDestroyed) {
            setFragmentResult(RESULT_KEY, bundleOf(RESULT_BUNDLE_KEY to true))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "MostrarRetoDialog"
        const val RESULT_KEY = "RETO_RESULT"
        const val RESULT_BUNDLE_KEY = "RETO_RESULT_CLOSED"
    }
}
