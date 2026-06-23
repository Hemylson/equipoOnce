package com.example.equipoonce.view.splash

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.equipoonce.R
import com.example.equipoonce.databinding.ActivitySplashBinding
import com.example.equipoonce.view.main.MainActivity
import kotlinx.coroutines.launch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        // lifecycleScope + repeatOnLifecycle garantiza que el observer se cancela
        // automáticamente si la Activity se destruye antes del delay
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navegarAlHome.collect { shouldNavigate ->
                    if (shouldNavigate) {
                        viewModel.onNavegado()
                        navigateToMain()
                    }
                }
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
