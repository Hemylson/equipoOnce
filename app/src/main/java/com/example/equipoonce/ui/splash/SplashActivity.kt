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
    private val handler = Handler(Looper.getMainLooper())
    private val navigateRunnable = Runnable { navigateToMain() }

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

        // CA-4: Navegar al home después del delay configurado
        handler.postDelayed(navigateRunnable, Constants.SPLASH_DELAY_MS)
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(navigateRunnable)
    }
}