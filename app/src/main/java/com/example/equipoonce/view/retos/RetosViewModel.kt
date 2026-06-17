package com.example.equipoonce.view.retos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.model.RetoEntity
import com.example.equipoonce.repository.RetoRepository
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

    // Mensaje puntual (validación / errores de operación). Es un evento, no un estado
    // de pantalla: no debe pisar la lista ya visible. Sigue el patrón _spinEvent/onSpinEventConsumed.
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

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
            _mensaje.value = error
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
            _mensaje.value = error
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

    // Limpia el evento una vez mostrado, para que no se repita en recomposiciones/rotaciones.
    fun onMensajeMostrado() { _mensaje.value = null }

    private fun ejecutar(bloque: suspend () -> Unit) {
        viewModelScope.launch {
            _uiState.value = RetosUiState.Loading
            try {
                bloque()
            } catch (e: Exception) {
                Timber.e(e, "Error en operación de retos")
                _mensaje.value = "Operación fallida. Intenta de nuevo."
                // El error es un evento puntual; restauramos la lista para no dejar la pantalla en Loading.
                runCatching {
                    val lista = repository.obtenerTodos()
                    _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
                }
            }
        }
    }
}
