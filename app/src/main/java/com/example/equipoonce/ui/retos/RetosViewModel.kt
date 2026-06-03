package com.example.equipoonce.ui.retos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.data.repository.RetoRepository
import kotlinx.coroutines.launch

class RetosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RetoRepository(application)

    private val _uiState = MutableLiveData<RetosUiState>(RetosUiState.Loading)
    val uiState: LiveData<RetosUiState> get() = _uiState

    fun cargarRetos() = ejecutar {
        val lista = repository.obtenerTodos()
        _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
    }

    fun agregarReto(descripcion: String) = ejecutar {
        repository.insertar(RetoEntity(descripcion = descripcion))
        val lista = repository.obtenerTodos()
        _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
    }

    fun editarReto(reto: RetoEntity) = ejecutar {
        repository.actualizar(reto)
        val lista = repository.obtenerTodos()
        _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
    }

    fun eliminarReto(reto: RetoEntity) = ejecutar {
        repository.eliminar(reto)
        val lista = repository.obtenerTodos()
        _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
    }

    private fun ejecutar(bloque: suspend () -> Unit) {
        viewModelScope.launch {
            _uiState.value = RetosUiState.Loading
            try {
                bloque()
            } catch (e: Exception) {
                _uiState.value = RetosUiState.Error("Operación fallida. Intenta de nuevo.")
            }
        }
    }
}
