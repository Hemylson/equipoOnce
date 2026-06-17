package com.example.equipoonce.view.retos

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
import kotlinx.coroutines.launch
import com.example.equipoonce.databinding.FragmentRetosBinding
import com.example.equipoonce.di.RepositoryProvider
import com.example.equipoonce.di.RetosViewModelFactory
import com.example.equipoonce.view.adapter.RetosAdapter
import com.example.equipoonce.view.retos.dialogs.AgregarRetoDialog
import com.example.equipoonce.view.retos.dialogs.EditarRetoDialog
import com.example.equipoonce.view.retos.dialogs.EliminarRetoDialog

class RetosFragment : Fragment() {

    private var _binding: FragmentRetosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RetosViewModel by viewModels {
        RetosViewModelFactory(RepositoryProvider.provideRetoRepository())
    }
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
                // El Fragment pasa datos crudos — el ViewModel decide cómo transformarlos
                EditarRetoDialog.show(requireContext(), reto.descripcion) { nuevaDescripcion ->
                    viewModel.editarReto(reto.id, nuevaDescripcion)
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
                // Estado de la lista
                launch {
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
                        }
                    }
                }
                // Mensajes puntuales (validación / errores): Toast sin alterar la lista
                launch {
                    viewModel.mensaje.collect { mensaje ->
                        if (mensaje != null) {
                            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                            viewModel.onMensajeMostrado()
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
