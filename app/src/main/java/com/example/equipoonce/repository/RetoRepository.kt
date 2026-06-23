package com.example.equipoonce.repository

import com.example.equipoonce.model.Reto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
        // HU 7.0 C6: el reto más reciente (timestamp mayor) queda primero.
        retosCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()
            .documents.map { document ->
                Reto(
                    id = document.id,
                    descripcion = document.getString("descripcion") ?: "",
                    timestamp = document.getLong("timestamp") ?: 0L
                )
            }
    }

    suspend fun actualizar(reto: Reto) = withContext(Dispatchers.IO) {
        require(reto.id.isNotBlank()) { "El reto debe tener un id válido para actualizarse." }
        // Solo se actualiza la descripción para conservar la posición original en la lista.
        retosCollection.document(reto.id).update("descripcion", reto.descripcion).await()
    }

    suspend fun eliminar(reto: Reto) = withContext(Dispatchers.IO) {
        require(reto.id.isNotBlank()) { "El reto debe tener un id válido para eliminarse." }
        retosCollection.document(reto.id).delete().await()
    }
}
