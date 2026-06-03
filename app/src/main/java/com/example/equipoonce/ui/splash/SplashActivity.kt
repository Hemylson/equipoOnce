package com.example.equipoonce.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.equipoonce.R
import com.example.equipoonce.databinding.ActivitySplashBinding
import com.example.equipoonce.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.ivBottle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_bottle_bounce))
        binding.tvPicoBotella.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_fade_in))

        viewModel.navegarAlHome.observe(this) { shouldNavigate ->
            if (shouldNavigate) {
                viewModel.onNavegado()
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
