package com.example.equipoonce.repository

import com.example.equipoonce.model.Reto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repositorio de retos respaldado por Firestore.
 */
class RetoRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    private val retosCollection = firestore.collection("retos")

    suspend fun insertar(reto: Reto) = withContext(Dispatchers.IO) {
        val document = retosCollection.document()
        val retoConId = reto.copy(id = document.id)
        document.set(retoConId).await()
    }

    suspend fun obtenerTodos(): List<Reto> = withContext(Dispatchers.IO) {
        retosCollection.get().await().documents.map { document ->
            val descripcion = document.getString("descripcion") ?: ""
            Reto(id = document.id, descripcion = descripcion)
        }
    }

    suspend fun actualizar(reto: Reto) = withContext(Dispatchers.IO) {
        require(reto.id.isNotBlank()) { "El reto debe tener un id válido para actualizarse." }
        retosCollection.document(reto.id).set(reto).await()
    }

    suspend fun eliminar(reto: Reto) = withContext(Dispatchers.IO) {
        require(reto.id.isNotBlank()) { "El reto debe tener un id válido para eliminarse." }
        retosCollection.document(reto.id).delete().await()
    }
}
