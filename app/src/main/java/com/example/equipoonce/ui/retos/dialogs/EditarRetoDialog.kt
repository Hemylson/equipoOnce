package com.example.equipoonce.ui.retos.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.equipoonce.databinding.DialogEditarRetoBinding

class EditarRetoDialog {
    companion object {
        fun show(
            context: Context,
            descripcionActual: String,
            onGuardar: (String) -> Unit
        ) {
            val binding = DialogEditarRetoBinding.inflate(
                LayoutInflater.from(context)
            )
            val dialog = AlertDialog.Builder(context)
                .setView(binding.root)
                .create()

            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawableResource(
                android.R.color.transparent
            )

            // Precarga el texto actual del reto
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

