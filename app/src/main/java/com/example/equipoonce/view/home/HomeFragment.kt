package com.example.equipoonce.view.home

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.imageLoader
import coil.request.ImageRequest
import com.example.equipoonce.R
import com.example.equipoonce.view.challenge.ChallengeUiState
import com.example.equipoonce.view.challenge.ChallengeViewModel
import com.example.equipoonce.view.shared.SharedAudioViewModel
import com.example.equipoonce.view.challenge.MostrarRetoDialog
import com.example.equipoonce.utils.Constants
import kotlinx.coroutines.launch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private val audioViewModel: SharedAudioViewModel by activityViewModels()
    private val challengeViewModel: ChallengeViewModel by activityViewModels()

    private lateinit var btnPresioname: View
    private lateinit var circlePresioname: View
    private lateinit var tvContador: TextView
    private lateinit var imgBotella: ImageView
    private lateinit var btnAudio: ImageButton
    private var currentBottleAnimator: ObjectAnimator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnPresioname = view.findViewById(R.id.btnPresioname)
        circlePresioname = view.findViewById(R.id.circlePresioname)
        tvContador = view.findViewById(R.id.tvContador)
        imgBotella = view.findViewById(R.id.imgBotella)

        audioViewModel.startBackground()
        configurarToolbar(view)
        configurarBoton()
        configurarDialogResultListener()
        observarViewModel()
        precargarImagenPokemon()
    }

    override fun onResume() {
        super.onResume()
        audioViewModel.resumeIfNeeded()
    }

    override fun onPause() {
        super.onPause()
        audioViewModel.pauseBackground()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        circlePresioname.clearAnimation()
        currentBottleAnimator?.cancel()
        currentBottleAnimator = null
        audioViewModel.pauseForNavigation()
    }

    // ── Toolbar ────────────────────────────────────────────────────────────

    private fun configurarToolbar(view: View) {
        view.findViewById<ImageButton>(R.id.btnCalificar).setOnClickListener {
            animarBoton(it)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PLAY_STORE_URL)))
        }

        view.findViewById<ImageButton>(R.id.btnCompartir).setOnClickListener {
            animarBoton(it)
            val mensaje = "${Constants.APP_SHARE_TITLE}\n${Constants.APP_SHARE_SLOGAN}\n${Constants.PLAY_STORE_URL}"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, mensaje)
            }
            startActivity(Intent.createChooser(intent, "Compartir via"))
        }

        btnAudio = view.findViewById(R.id.btnAudio)
        btnAudio.setOnClickListener {
            animarBoton(it)
            audioViewModel.toggleAudio()
        }

        view.findViewById<ImageButton>(R.id.btnRetos).setOnClickListener {
            animarBoton(it)
            findNavController().navigate(R.id.action_home_to_retos)
        }

        view.findViewById<ImageButton>(R.id.btnInstrucciones).setOnClickListener {
            animarBoton(it)
            findNavController().navigate(R.id.action_home_to_instructions)
        }
    }

    private fun animarBoton(view: View) {
        AlphaAnimation(1f, 0.3f).apply {
            duration = 150
            repeatMode = AlphaAnimation.REVERSE
            repeatCount = 1
        }.also { view.startAnimation(it) }
    }

    // ── Botón presióname ───────────────────────────────────────────────────

    private fun iniciarAnimacionBoton() {
        AlphaAnimation(0.3f, 1f).apply {
            duration = 700
            repeatMode = AlphaAnimation.REVERSE
            repeatCount = AlphaAnimation.INFINITE
        }.also { circlePresioname.startAnimation(it) }
    }

    private fun configurarBoton() {
        iniciarAnimacionBoton()
        btnPresioname.setOnClickListener {
            challengeViewModel.cargarRetoYPokemon()
            viewModel.onPresionameClicked()
        }
    }

    // ── Dialog result ──────────────────────────────────────────────────────

    private fun configurarDialogResultListener() {
        setFragmentResultListener(MostrarRetoDialog.RESULT_KEY) { _, bundle ->
            if (bundle.getBoolean(MostrarRetoDialog.KEY_DIALOG_CLOSED, false)) {
                viewModel.onDialogClosed()
                audioViewModel.onSpinDialogClosed()
            }
        }
    }

    // ── Observadores ───────────────────────────────────────────────────────

    private fun observarViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contador.collect { valor ->
                    tvContador.visibility = if (valor != null) View.VISIBLE else View.INVISIBLE
                    tvContador.text = valor?.toString() ?: ""
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isButtonVisible.collect { visible ->
                    if (visible) {
                        btnPresioname.visibility = View.VISIBLE
                        btnPresioname.isEnabled = true
                        iniciarAnimacionBoton()
                    } else {
                        circlePresioname.clearAnimation()
                        circlePresioname.alpha = 1f
                        btnPresioname.visibility = View.INVISIBLE
                        btnPresioname.isEnabled = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                audioViewModel.isAudioOn.collect { audioOn ->
                    btnAudio.setImageResource(
                        if (audioOn) R.drawable.ic_volume_up else R.drawable.ic_volume_off
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.spinEvent.collect { params ->
                    if (params != null && isAdded && !parentFragmentManager.isStateSaved) {
                        playBottleSpin(params)
                        viewModel.onSpinEventConsumed()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showDialog.collect { shouldShow ->
                    if (shouldShow && isAdded && !parentFragmentManager.isStateSaved) {
                        val yaExiste = parentFragmentManager.findFragmentByTag(MostrarRetoDialog.TAG)
                        if (yaExiste == null) {
                            MostrarRetoDialog.newInstance().show(parentFragmentManager, MostrarRetoDialog.TAG)
                        }
                        viewModel.onDialogShown()
                    }
                }
            }
        }
    }

    // ── Animación botella ──────────────────────────────────────────────────

    private fun playBottleSpin(params: SpinParams) {
        currentBottleAnimator?.cancel()
        currentBottleAnimator = ObjectAnimator.ofFloat(
            imgBotella, View.ROTATION, imgBotella.rotation, params.targetAngle
        ).apply {
            duration = params.durationMs
            interpolator = DecelerateInterpolator(1.8f)
            doOnStart {
                imgBotella.setLayerType(View.LAYER_TYPE_HARDWARE, null)
                audioViewModel.onSpinStart()
            }
            doOnEnd {
                audioViewModel.onSpinEnd()
                imgBotella.rotation = imgBotella.rotation % 360f
                imgBotella.setLayerType(View.LAYER_TYPE_NONE, null)
                currentBottleAnimator = null
            }
            start()
        }
    }

    // ── Precarga imagen pokemon ────────────────────────────────────────────

    private fun precargarImagenPokemon() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                challengeViewModel.uiState.collect { state ->
                    if (state is ChallengeUiState.Success) {
                        state.pokemon?.img?.let { url ->
                            val request = ImageRequest.Builder(requireContext()).data(url).build()
                            requireContext().imageLoader.enqueue(request)
                        }
                    }
                }
            }
        }
    }
}
