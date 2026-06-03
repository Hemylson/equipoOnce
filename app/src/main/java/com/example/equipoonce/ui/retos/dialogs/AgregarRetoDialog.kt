package com.example.equipoonce.ui.retos.dialogs

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import com.example.equipoonce.databinding.DialogAgregarRetoBinding
import com.example.equipoonce.utils.crearDialogReto

class AgregarRetoDialog {
    companion object {
        fun show(context: Context, onGuardar: (String) -> Unit) {
            val binding = DialogAgregarRetoBinding.inflate(LayoutInflater.from(context))
            val dialog = context.crearDialogReto(binding.root)

            binding.etReto.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
                override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val tieneTexto = !s.isNullOrBlank()
                    binding.btnGuardar.isEnabled = tieneTexto
                    binding.btnGuardar.alpha = if (tieneTexto) 1f else 0.5f
                    binding.btnGuardar.refreshDrawableState()
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
