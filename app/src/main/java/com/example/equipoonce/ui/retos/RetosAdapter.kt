package com.example.equipoonce.ui.retos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.databinding.ItemRetoBinding

class RetosAdapter(
    private val onEdit: (RetoEntity) -> Unit,
    private val onDelete: (RetoEntity) -> Unit
) : ListAdapter<RetoEntity, RetosAdapter.RetoViewHolder>(DiffCallback()) {

    companion object {
        // Delay para que el ripple del Material Design sea visible antes de ejecutar la acción
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

        fun bind(reto: RetoEntity) {
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

    private class DiffCallback : DiffUtil.ItemCallback<RetoEntity>() {
        override fun areItemsTheSame(old: RetoEntity, new: RetoEntity) = old.id == new.id
        override fun areContentsTheSame(old: RetoEntity, new: RetoEntity) = old == new
    }
}
