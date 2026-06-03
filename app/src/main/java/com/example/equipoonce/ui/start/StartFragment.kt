package com.example.equipoonce.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.equipoonce.R
import com.example.equipoonce.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnJugar.setOnClickListener {
            findNavController().navigate(R.id.action_start_to_home)
        }

        binding.btnInstrucciones.setOnClickListener {
            findNavController().navigate(R.id.action_start_to_instructions)
        }

        binding.btnRetos.setOnClickListener {
            findNavController().navigate(R.id.action_start_to_retos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
