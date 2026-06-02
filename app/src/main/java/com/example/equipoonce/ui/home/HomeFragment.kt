package com.example.equipoonce.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.data.repository.RetoRepository
import com.example.equipoonce.databinding.FragmentHomeBinding
import com.example.equipoonce.ui.challenge.MostrarRetoDialog
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Seed solo en DEBUG y únicamente si la tabla está vacía
        if (com.example.equipoonce.BuildConfig.DEBUG) {
            lifecycleScope.launch {
                val repo = RetoRepository(requireContext())
                if (repo.obtenerTodos().isEmpty()) {
                    repo.insertar(RetoEntity(descripcion = "Canta una canción completa"))
                    repo.insertar(RetoEntity(descripcion = "Imita a un animal por 30 segundos"))
                    repo.insertar(RetoEntity(descripcion = "Di un trabalenguas 3 veces"))
                }
            }
        }

        binding.btnPresioname.setOnClickListener {
            MostrarRetoDialog.newInstance()
                .show(parentFragmentManager, MostrarRetoDialog.TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
