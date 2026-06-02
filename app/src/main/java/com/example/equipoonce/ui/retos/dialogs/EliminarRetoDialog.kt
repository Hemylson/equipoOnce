package com.example.equipoonce.ui.retos.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EliminarRetoDialog : DialogFragment() {

    // TODO: recibir RetoEntity como argumento via Bundle
    // TODO: comunicar confirmación al ViewModel vía Fragment Result API

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar Reto")
            .setMessage("¿Estás seguro de que deseas eliminar este reto?")
            .setPositiveButton("Eliminar") { _, _ ->
                // TODO: llamar al ViewModel para ejecutar el delete
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    companion object {
        const val TAG = "EliminarRetoDialog"
    }
}
