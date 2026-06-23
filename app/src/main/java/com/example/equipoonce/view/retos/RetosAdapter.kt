package com.example.equipoonce.view.retos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.equipoonce.model.Reto
import com.example.equipoonce.databinding.ItemRetoBinding

class RetosAdapter(
    private val onEdit: (Reto) -> Unit,
    private val onDelete: (Reto) -> Unit
) : ListAdapter<Reto, RetosAdapter.RetoViewHolder>(DiffCallback()) {

    companion object {
        private const val TOUCH_FEEDBACK_DELAY_MS = 150L
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        val binding = ItemRetoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RetoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RetoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RetoViewHolder(
        private val binding: ItemRetoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reto: Reto) {
            binding.tvDescripcionReto.text = reto.descripcion
            binding.btnEditar.setOnClickListener { view ->
                view.isEnabled = false
                view.postDelayed({
                    if (view.isAttachedToWindow) onEdit(reto)
                    view.isEnabled = true
                }, TOUCH_FEEDBACK_DELAY_MS)
            }
            binding.btnEliminar.setOnClickListener { view ->
                view.isEnabled = false
                view.postDelayed({
                    if (view.isAttachedToWindow) onDelete(reto)
                    view.isEnabled = true
                }, TOUCH_FEEDBACK_DELAY_MS)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Reto>() {
        override fun areItemsTheSame(old: Reto, new: Reto) = old.id == new.id
        override fun areContentsTheSame(old: Reto, new: Reto) = old == new
    }
}
