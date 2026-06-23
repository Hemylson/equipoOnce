package com.example.equipoonce.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Repositorio de autenticación respaldado por Firebase Auth.
 * HU 1.0 / 2.0 — login, registro, logout y sesión actual.
 */
class AuthRepository @Inject constructor(private val auth: FirebaseAuth) {

    fun usuarioActual(): FirebaseUser? = auth.currentUser

    fun haySesionActiva(): Boolean = auth.currentUser != null

    suspend fun login(email: String, password: String): FirebaseUser {
        val resultado = auth.signInWithEmailAndPassword(email, password).await()
        return resultado.user ?: throw IllegalStateException("Usuario nulo tras iniciar sesión.")
    }

    suspend fun registrar(email: String, password: String): FirebaseUser {
        val resultado = auth.createUserWithEmailAndPassword(email, password).await()
        return resultado.user ?: throw IllegalStateException("Usuario nulo tras registrarse.")
    }

    fun logout() = auth.signOut()
}
