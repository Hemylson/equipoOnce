package com.example.equipoonce.ui.retos.dialogs

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import com.example.equipoonce.databinding.DialogAgregarRetoBinding

// HU 7.0 - Dialog agregar reto - ALAN LEAL
class AgregarRetoDialog {
    companion object {
        fun show(context: Context, onGuardar: (String) -> Unit) {
            val binding = DialogAgregarRetoBinding.inflate(
                LayoutInflater.from(context)
            )
            val dialog = AlertDialog.Builder(context)
                .setView(binding.root)
                .create()

            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawableResource(
                android.R.color.transparent
            )

            // Habilitar/deshabilitar boton Guardar segun texto
            binding.etReto.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
                override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    binding.btnGuardar.isEnabled = !s.isNullOrBlank()
                }
            })

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

