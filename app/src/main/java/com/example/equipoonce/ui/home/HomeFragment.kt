package com.example.equipoonce.ui.home

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.equipoonce.databinding.FragmentHomeBinding
import com.example.equipoonce.ui.challenge.MostrarRetoDialog
import com.example.equipoonce.utils.GameAudioManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var audioManager: GameAudioManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        audioManager = GameAudioManager(requireContext())
        audioManager.playBackground()

        configurarToolbar()
        configurarBoton()
        observarViewModel()

        childFragmentManager.setFragmentResultListener(
            MostrarRetoDialog.RESULT_KEY,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.onRetoDialogClosed()
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isSpinning.value != true) {
            audioManager.resumeBackground()
        }
    }

    override fun onPause() {
        super.onPause()
        audioManager.pauseBackground()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        audioManager.stopAllSounds()
        _binding = null
    }

    private fun configurarToolbar() {
        binding.toolbar.btnCalificar.setOnClickListener {
            animarBoton(it)
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.nequi.MobileApp")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        binding.toolbar.btnCompartir.setOnClickListener {
            animarBoton(it)
            val mensaje = "App pico botella\nSolo los valientes lo juegan !!\nhttps://play.google.com/store/apps/details?id=com.nequi.MobileApp"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, mensaje)
            }
            startActivity(Intent.createChooser(intent, "Compartir via"))
        }

        binding.toolbar.btnAudio.setOnClickListener {
            animarBoton(it)
            if (audioManager.isBackgroundPlaying()) {
                audioManager.pauseBackground()
            } else {
                audioManager.resumeBackground()
            }
        }
    }

    private fun animarBoton(view: View) {
        val anim = AlphaAnimation(1f, 0.3f).apply {
            duration = 150
            repeatMode = AlphaAnimation.REVERSE
            repeatCount = 1
        }
        view.startAnimation(anim)
    }

    private fun configurarBoton() {
        val anim = AlphaAnimation(0f, 1f).apply {
            duration = 600
            repeatMode = AlphaAnimation.REVERSE
            repeatCount = AlphaAnimation.INFINITE
        }
        binding.btnPresioname.startAnimation(anim)
        binding.btnPresioname.setOnClickListener {
            viewModel.girarBotella()
        }
    }

    private fun observarViewModel() {
        viewModel.isSpinning.observe(viewLifecycleOwner) { girando ->
            binding.btnPresioname.visibility = if (girando) View.INVISIBLE else View.VISIBLE
            binding.btnPresioname.isEnabled = !girando
            if (girando) {
                audioManager.pauseBackground()
                audioManager.playSpinSound()
            }
        }

        viewModel.contador.observe(viewLifecycleOwner) { valor ->
            binding.tvContador.visibility = if (valor != null) View.VISIBLE else View.INVISIBLE
            binding.tvContador.text = valor?.toString() ?: ""
        }

        viewModel.rotationAngle.observe(viewLifecycleOwner) { angulo ->
            if (angulo != binding.imgBotella.rotation) {
                val animator = ObjectAnimator.ofFloat(
                    binding.imgBotella,
                    View.ROTATION,
                    binding.imgBotella.rotation,
                    angulo
                ).apply {
                    duration = viewModel.spinDuration.value ?: 4000L
                }
                animator.start()
            }
        }

        viewModel.showRetoDialogEvent.observe(viewLifecycleOwner) {
            mostrarDialogoReto()
        }
    }

    private fun mostrarDialogoReto() {
        MostrarRetoDialog().show(childFragmentManager, MostrarRetoDialog.TAG)
    }
}
