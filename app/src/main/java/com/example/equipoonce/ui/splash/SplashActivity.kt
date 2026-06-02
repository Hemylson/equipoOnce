package com.example.equipoonce.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.equipoonce.databinding.ActivitySplashBinding
import com.example.equipoonce.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // TODO: agregar animación del logo antes de navegar
        // TODO: reemplazar delay directo por animación con AnimatorSet o Lottie
        navigateToMain()
    }

    private fun navigateToMain() {
        // TODO: agregar Handler.postDelayed con Constants.SPLASH_DELAY_MS
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
