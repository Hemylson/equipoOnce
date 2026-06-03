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

    private val _lista = MutableLiveData<List<RetoEntity>>()
    val lista: LiveData<List<RetoEntity>> get() = _lista

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun onErrorConsumed() { _error.value = null }

    fun cargarRetos() = ejecutar("No se pudieron cargar los retos.") {
        _lista.value = repository.obtenerTodos()
    }

    fun agregarReto(descripcion: String) = ejecutar("No se pudo agregar el reto.") {
        repository.insertar(RetoEntity(descripcion = descripcion))
        _lista.value = repository.obtenerTodos()
    }

    fun editarReto(reto: RetoEntity) = ejecutar("No se pudo editar el reto.") {
        repository.actualizar(reto)
        _lista.value = repository.obtenerTodos()
    }

    fun eliminarReto(reto: RetoEntity) = ejecutar("No se pudo eliminar el reto.") {
        repository.eliminar(reto)
        _lista.value = repository.obtenerTodos()
    }

    private fun ejecutar(mensajeError: String, bloque: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                bloque()
            } catch (e: Exception) {
                _error.value = mensajeError
            }
        }
    }
}
