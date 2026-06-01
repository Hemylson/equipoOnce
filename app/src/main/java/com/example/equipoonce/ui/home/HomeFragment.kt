package com.example.equipoonce.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.equipoonce.R
import com.example.equipoonce.utils.GameAudioManager

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var audioManager: GameAudioManager
    private lateinit var btnPresioname: Button
    private lateinit var tvContador: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        audioManager = GameAudioManager(requireContext())
        audioManager.playBackground()

        btnPresioname = view.findViewById(R.id.btnPresioname)
        tvContador = view.findViewById(R.id.tvContador)

        configurarToolbar(view)
        configurarBoton()
        observarViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.isCounting()) {
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
    }

    private fun configurarToolbar(view: View) {
        view.findViewById<ImageButton>(R.id.btnCalificar).setOnClickListener {
            animarBoton(it)
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.nequi.MobileApp")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        view.findViewById<ImageButton>(R.id.btnCompartir).setOnClickListener {
            animarBoton(it)
            val mensaje = "App pico botella\nSolo los valientes lo juegan !!\nhttps://play.google.com/store/apps/details?id=com.nequi.MobileApp"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, mensaje)
            }
            startActivity(Intent.createChooser(intent, "Compartir via"))
        }

        view.findViewById<ImageButton>(R.id.btnAudio).setOnClickListener {
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
        btnPresioname.startAnimation(anim)
        btnPresioname.setOnClickListener {
            viewModel.onPresionameClicked()
        }
    }

    private fun observarViewModel() {
        viewModel.contador.observe(viewLifecycleOwner) { valor ->
            if (valor != null) {
                tvContador.visibility = View.VISIBLE
                tvContador.text = valor.toString()
            } else {
                tvContador.visibility = View.INVISIBLE
            }
        }

        viewModel.isButtonVisible.observe(viewLifecycleOwner) { visible ->
            btnPresioname.visibility = if (visible) View.VISIBLE else View.INVISIBLE
            btnPresioname.isEnabled = visible
        }
    }
}
