package com.example.equipoonce.ui.retos.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.equipoonce.databinding.DialogEliminarRetoBinding

class EliminarRetoDialog {
    companion object {
        fun show(
            context: Context,
            descripcion: String,
            onConfirmar: () -> Unit
        ) {
            val binding = DialogEliminarRetoBinding.inflate(
                LayoutInflater.from(context)
            )
            val dialog = AlertDialog.Builder(context)
                .setView(binding.root)
                .create()

            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawableResource(
                android.R.color.transparent
            )

            // Muestra la descripcion del reto a eliminar
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

