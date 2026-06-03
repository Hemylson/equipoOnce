package com.example.equipoonce.ui.retos.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.example.equipoonce.databinding.DialogEditarRetoBinding
import com.example.equipoonce.utils.crearDialogReto

class EditarRetoDialog {
    companion object {
        fun show(context: Context, descripcionActual: String, onGuardar: (String) -> Unit) {
            val binding = DialogEditarRetoBinding.inflate(LayoutInflater.from(context))
            val dialog = context.crearDialogReto(binding.root)

            binding.etReto.setText(descripcionActual)

            binding.btnCancelar.setOnClickListener { dialog.dismiss() }

            binding.btnGuardar.setOnClickListener {
                val descripcion = binding.etReto.text.toString().trim()
                if (descripcion.isNotEmpty()) {
                    onGuardar(descripcion)
                    dialog.dismiss()
                }
            }

            dialog.show()
        }
    }
}
