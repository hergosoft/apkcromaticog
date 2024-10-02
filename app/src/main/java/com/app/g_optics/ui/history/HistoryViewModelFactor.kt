package com.app.g_optics.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.g_optics.repositories.history.HistoryRepository

// Fábrica de ViewModel para crear instancias de HistoryViewModel con el repositorio correspondiente
class HistoryViewModelFactory(private val repository: HistoryRepository) : ViewModelProvider.Factory {
    // Método que crea el ViewModel si la clase solicitada es HistoryViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T // Retorna una instancia de HistoryViewModel
        }
        throw IllegalArgumentException("Unknown ViewModel class") // Lanza excepción si no es la clase esperada
    }
}
