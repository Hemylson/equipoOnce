package com.example.equipoonce.ui.retos.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.equipoonce.databinding.DialogAgregarRetoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AgregarRetoDialog : DialogFragment() {

    private var _binding: DialogAgregarRetoBinding? = null
    private val binding get() = _binding!!

    // TODO: comunicar el nuevo reto al ViewModel compartido vía Fragment Result API

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAgregarRetoBinding.inflate(layoutInflater)
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Agregar Reto")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                // TODO: leer etDescripcion y crear RetoEntity para enviar al ViewModel
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AgregarRetoDialog"
    }
}
