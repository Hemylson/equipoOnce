package com.example.equipoonce.ui.retos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.equipoonce.databinding.FragmentRetosBinding
import com.example.equipoonce.ui.retos.dialogs.AgregarRetoDialog
import com.example.equipoonce.ui.retos.dialogs.EditarRetoDialog
import com.example.equipoonce.ui.retos.dialogs.EliminarRetoDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RetosFragment : Fragment() {

    private var _binding: FragmentRetosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RetosViewModel by viewModels()
    private lateinit var adapter: RetosAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
            findNavController().navigateUp()
        }
    }

    private fun configurarRecyclerView() {
        adapter = RetosAdapter(
            onEdit = { reto ->
                EditarRetoDialog.show(requireContext(), reto.descripcion) { nuevaDescripcion ->
                    viewModel.editarReto(reto.copy(descripcion = nuevaDescripcion))
                }
            },
            onDelete = { reto ->
                EliminarRetoDialog.show(requireContext(), reto.descripcion) {
                    viewModel.eliminarReto(reto)
                }
            }
        )
        binding.rvRetos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRetos.adapter = adapter
    }

    private fun observarViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvRetos.visibility = View.GONE

                    when (state) {
                        is RetosUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is RetosUiState.Success -> {
                            binding.rvRetos.visibility = View.VISIBLE
                            adapter.submitList(state.retos)
                        }
                        is RetosUiState.Empty -> {
                            binding.tvEmpty.visibility = View.VISIBLE
                        }
                        is RetosUiState.Error -> {
                            binding.tvEmpty.visibility = View.VISIBLE
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun configurarFab() {
        binding.fabAgregarReto.setOnClickListener {
            AgregarRetoDialog.show(requireContext()) { descripcion ->
                viewModel.agregarReto(descripcion)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
