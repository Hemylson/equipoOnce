package com.example.equipoonce.ui.retos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.equipoonce.databinding.FragmentRetosBinding

class RetosFragment : Fragment() {

    private var _binding: FragmentRetosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RetosViewModel by viewModels()
    private lateinit var adapter: RetosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRetosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarToolbar()
        configurarRecyclerView()
        observarViewModel()
        configurarFab()
        viewModel.cargarRetos()
    }

    private fun configurarToolbar() {
        binding.toolbarRetos.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun configurarRecyclerView() {
        adapter = RetosAdapter(
            onEdit = { reto ->
                // TODO: HU 8.0 - Alan implementa EditarRetoDialog
            },
            onDelete = { reto ->
                // TODO: HU 9.0 - Alan implementa EliminarRetoDialog
            }
        )
        binding.rvRetos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRetos.adapter = adapter
    }

    private fun observarViewModel() {
        viewModel.lista.observe(viewLifecycleOwner) { lista ->
            adapter.actualizarLista(lista)
        }
    }

    private fun configurarFab() {
        binding.fabAgregarReto.setOnClickListener {
            // TODO: HU 7.0 - Alan implementa AgregarRetoDialog
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
