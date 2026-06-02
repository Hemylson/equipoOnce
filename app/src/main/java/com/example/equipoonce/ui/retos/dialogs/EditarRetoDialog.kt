package com.example.equipoonce.ui.retos.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.equipoonce.databinding.DialogEditarRetoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditarRetoDialog : DialogFragment() {

    private var _binding: DialogEditarRetoBinding? = null
    private val binding get() = _binding!!

    // TODO: recibir RetoEntity como argumento via Bundle y pre-poblar campos

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogEditarRetoBinding.inflate(layoutInflater)
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Editar Reto")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                // TODO: actualizar RetoEntity con valores nuevos del formulario
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "EditarRetoDialog"
    }
}
