package com.example.equipoonce.ui.retos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.databinding.ItemRetoBinding

class RetosAdapter(
    private val onEdit: (RetoEntity) -> Unit,
    private val onDelete: (RetoEntity) -> Unit
) : RecyclerView.Adapter<RetosAdapter.RetoViewHolder>() {

    private var lista: List<RetoEntity> = emptyList()

    fun actualizarLista(nuevaLista: List<RetoEntity>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        val binding = ItemRetoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RetoViewHolder(binding)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: RetoViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    inner class RetoViewHolder(
        private val binding: ItemRetoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reto: RetoEntity) {
            binding.tvDescripcionReto.text = reto.descripcion
            binding.btnEditar.setOnClickListener { onEdit(reto) }
            binding.btnEliminar.setOnClickListener { onDelete(reto) }
        }
    }
}
