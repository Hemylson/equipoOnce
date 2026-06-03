package com.example.equipoonce.ui.retos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.data.repository.RetoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel de Retos. Recibe el repositorio por constructor (DI manual).
 * Hereda de ViewModel puro — sin referencias a Android.
 * Toda la validación de negocio vive aquí, no en la Vista.
 */
class RetosViewModel(private val repository: RetoRepository) : ViewModel() {

    companion object {
        private const val MIN_DESCRIPCION_LENGTH = 3
        private const val MAX_DESCRIPCION_LENGTH = 200
    }

    private val _uiState = MutableStateFlow<RetosUiState>(RetosUiState.Loading)
    val uiState: StateFlow<RetosUiState> = _uiState

    fun cargarRetos() = ejecutar {
        Timber.d("Cargando retos...")
        val lista = repository.obtenerTodos()
        Timber.d("Retos cargados: ${lista.size}")
        _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
    }

    fun agregarReto(descripcion: String) {
        val error = validarDescripcion(descripcion)
        if (error != null) {
            Timber.w("Validación fallida al agregar: $error")
            _uiState.value = RetosUiState.Error(error)
            return
        }
        ejecutar {
            repository.insertar(RetoEntity(descripcion = descripcion.trim()))
            Timber.d("Reto agregado: ${descripcion.trim()}")
            val lista = repository.obtenerTodos()
            _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
        }
    }

    fun editarReto(id: Int, nuevaDescripcion: String) {
        val error = validarDescripcion(nuevaDescripcion)
        if (error != null) {
            Timber.w("Validación fallida al editar: $error")
            _uiState.value = RetosUiState.Error(error)
            return
        }
        ejecutar {
            repository.actualizar(RetoEntity(id = id, descripcion = nuevaDescripcion.trim()))
            Timber.d("Reto editado id=$id")
            val lista = repository.obtenerTodos()
            _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
        }
    }

    fun eliminarReto(reto: RetoEntity) = ejecutar {
        repository.eliminar(reto)
        Timber.d("Reto eliminado id=${reto.id}")
        val lista = repository.obtenerTodos()
        _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
    }

    // Validación de negocio centralizada en el ViewModel
    private fun validarDescripcion(descripcion: String): String? {
        val trimmed = descripcion.trim()
        return when {
            trimmed.isBlank() -> "El reto no puede estar vacío."
            trimmed.length < MIN_DESCRIPCION_LENGTH -> "El reto debe tener al menos $MIN_DESCRIPCION_LENGTH caracteres."
            trimmed.length > MAX_DESCRIPCION_LENGTH -> "El reto no puede superar $MAX_DESCRIPCION_LENGTH caracteres."
            else -> null
        }
    }

    private fun ejecutar(bloque: suspend () -> Unit) {
        viewModelScope.launch {
            _uiState.value = RetosUiState.Loading
            try {
                bloque()
            } catch (e: Exception) {
                Timber.e(e, "Error en operación de retos")
                _uiState.value = RetosUiState.Error("Operación fallida. Intenta de nuevo.")
            }
        }
    }
}
