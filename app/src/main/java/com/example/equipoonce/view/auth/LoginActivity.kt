package com.example.equipoonce.view.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.equipoonce.R
import com.example.equipoonce.databinding.ActivityLoginBinding
import com.example.equipoonce.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * HU 1.0 / 2.0 — Pantalla de login y registro con Firebase Auth.
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        configurarValidacionEnVivo()
        configurarBotones()
        observarEstado()
    }

    private fun configurarValidacionEnVivo() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) = validarFormulario()
        }
        binding.etEmail.addTextChangedListener(watcher)
        binding.etPassword.addTextChangedListener(watcher)
    }

    private fun validarFormulario() {
        val email = binding.etEmail.text?.toString().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()

        binding.tilEmail.error =
            if (email.isNotEmpty() && !viewModel.emailValido(email)) getString(R.string.auth_error_email) else null
        binding.tilPassword.error =
            if (password.isNotEmpty() && !viewModel.passwordValida(password)) getString(R.string.auth_error_password) else null

        val valido = viewModel.formularioValido(email, password)
        binding.btnLogin.isEnabled = valido
        binding.btnRegistrar.isEnabled = valido
    }

    private fun configurarBotones() {
        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }
        binding.btnRegistrar.setOnClickListener {
            viewModel.registrar(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }
    }

    private fun observarEstado() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { estado ->
                    when (estado) {
                        is AuthUiState.Idle -> mostrarCargando(false)
                        is AuthUiState.Loading -> mostrarCargando(true)
                        is AuthUiState.Success -> {
                            mostrarCargando(false)
                            irAHome()
                        }
                        is AuthUiState.Error -> {
                            mostrarCargando(false)
                            Toast.makeText(this@LoginActivity, estado.message, Toast.LENGTH_LONG).show()
                            viewModel.consumirEstado()
                        }
                    }
                }
            }
        }
    }

    private fun mostrarCargando(cargando: Boolean) {
        binding.progress.visibility = if (cargando) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !cargando && binding.btnLogin.isEnabled
        binding.btnRegistrar.isEnabled = !cargando && binding.btnRegistrar.isEnabled
    }

    private fun irAHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
