package com.example.equipoonce.ui.retos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipoonce.data.local.RetoEntity
import com.example.equipoonce.domain.exception.AppException
import com.example.equipoonce.domain.usecase.AddRetoUseCase
import com.example.equipoonce.domain.usecase.DeleteRetoUseCase
import com.example.equipoonce.domain.usecase.EditRetoUseCase
import com.example.equipoonce.domain.usecase.GetAllRetosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RetosViewModel @Inject constructor(
    private val getAllRetos: GetAllRetosUseCase,
    private val addReto: AddRetoUseCase,
    private val editReto: EditRetoUseCase,
    private val deleteReto: DeleteRetoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RetosUiState>(RetosUiState.Loading)
    val uiState: StateFlow<RetosUiState> = _uiState

    fun cargarRetos() = ejecutar {
        val lista = getAllRetos()
        _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
    }

    fun agregarReto(descripcion: String) = ejecutar {
        addReto(descripcion)
        val lista = getAllRetos()
        _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
    }

    fun editarReto(reto: RetoEntity) = ejecutar {
        editReto(reto)
        val lista = getAllRetos()
        _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
    }

    fun eliminarReto(reto: RetoEntity) = ejecutar {
        deleteReto(reto)
        val lista = getAllRetos()
        _uiState.value = if (lista.isEmpty()) RetosUiState.Empty else RetosUiState.Success(lista)
    }

    private fun ejecutar(bloque: suspend () -> Unit) {
        viewModelScope.launch {
            _uiState.value = RetosUiState.Loading
            try {
                bloque()
            } catch (e: AppException.DatabaseException) {
                _uiState.value = RetosUiState.Error(e.message ?: "Error en base de datos.")
            } catch (e: AppException.EmptyResultException) {
                _uiState.value = RetosUiState.Error(e.message ?: "Datos inválidos.")
            } catch (e: Exception) {
                _uiState.value = RetosUiState.Error("Operación fallida. Intenta de nuevo.")
            }
        }
    }
}
