package com.example.equipoonce.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.equipoonce.R
import com.example.equipoonce.databinding.ActivitySplashBinding
import com.example.equipoonce.ui.main.MainActivity
import com.example.equipoonce.utils.Constants

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

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

        // CA-2: Animación de la botella (bounce + rotate)
        val bounceAnim = AnimationUtils.loadAnimation(this, R.anim.anim_bottle_bounce)
        binding.ivBottle.startAnimation(bounceAnim)

        // CA-3: Animación de fade in para el texto naranja
        val fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in)
        binding.tvPicoBotella.startAnimation(fadeInAnim)

        // CA-4: Navegar al home después de 5 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToMain()
        }, Constants.SPLASH_DELAY_MS)
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // CA-5: finish() evita regresar al splash con el botón atrás
    }
}