package com.app.g_optics.ui.history

import com.app.g_optics.core.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.g_optics.models.history.HistoryRequest
import com.app.g_optics.models.history.HistoryResponse
import com.app.g_optics.repositories.history.HistoryRepository

// ViewModel para gestionar el historial de datos
class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
    // LiveData que almacena el resultado de la operación de guardar historial
    private val _historyResult = MutableLiveData<Result<HistoryResponse>>()
    val historyResult: LiveData<Result<HistoryResponse>> = _historyResult

    // Método para guardar el historial llamando al repositorio
    fun saveHistory(request: HistoryRequest) {
        repository.saveHistory(request) { result ->
            _historyResult.value = result // Actualiza el LiveData con el resultado
        }
    }
}
