package com.example.equipoonce.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.equipoonce.R
import com.example.equipoonce.databinding.ActivitySplashBinding
import com.example.equipoonce.ui.main.MainActivity
// TODO (HU 2.0 - Login): cuando exista LoginActivity, descomentar este import
// import com.example.equipoonce.ui.login.LoginActivity
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // CA-1: Sin toolbar
        supportActionBar?.hide()

        // CA-1: Pantalla completa sin status bar
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Animaciones de la botella y el texto
        val bounceAnim = AnimationUtils.loadAnimation(this, R.anim.anim_bottle_bounce)
        binding.ivBottle.startAnimation(bounceAnim)

        val fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in)
        binding.tvPicoBotella.startAnimation(fadeInAnim)

        // CA-3: El back en el splash cierra la app, no navega hacia atrás
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })

        observarDestino()
        viewModel.iniciarSplash()
    }

    private fun observarDestino() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.destination.collect { destino ->
                    when (destino) {
                        is SplashDestination.Home -> navigateToMain()
                        is SplashDestination.Login -> navigateToLogin()
                        null -> Unit // todavía esperando los 5 segundos
                    }
                }
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /**
     * TODO (HU 2.0 - Login): reemplazar el cuerpo de este método por:
     *
     *   startActivity(Intent(this, LoginActivity::class.java))
     *   finish()
     *
     * Mientras tanto, navega al Home para no romper la app en desarrollo.
     */
    private fun navigateToLogin() {
        navigateToMain()
    }
}