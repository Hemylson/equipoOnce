package com.example.equipoonce.view.retos.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.example.equipoonce.databinding.DialogEliminarRetoBinding
import com.example.equipoonce.utils.crearDialogReto

class EliminarRetoDialog {
    companion object {
        fun show(context: Context, descripcion: String, onConfirmar: () -> Unit) {
            val binding = DialogEliminarRetoBinding.inflate(LayoutInflater.from(context))
            val dialog = context.crearDialogReto(binding.root)

            binding.tvDescripcionReto.text = descripcion

            binding.btnNo.setOnClickListener { dialog.dismiss() }

            binding.btnSi.setOnClickListener {
                onConfirmar()
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}
