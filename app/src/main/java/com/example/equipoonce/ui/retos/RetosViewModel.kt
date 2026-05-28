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

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    fun cargarRetos() {
        viewModelScope.launch {
            _loading.value = true
            _lista.value = repository.obtenerTodos()
            _loading.value = false
        }
    }

    fun agregarReto(descripcion: String) {
        viewModelScope.launch {
            repository.insertar(RetoEntity(descripcion = descripcion))
            cargarRetos()
        }
    }

    fun editarReto(reto: RetoEntity) {
        viewModelScope.launch {
            repository.actualizar(reto)
            cargarRetos()
        }
    }

    fun eliminarReto(reto: RetoEntity) {
        viewModelScope.launch {
            repository.eliminar(reto)
            cargarRetos()
        }
    }
}
