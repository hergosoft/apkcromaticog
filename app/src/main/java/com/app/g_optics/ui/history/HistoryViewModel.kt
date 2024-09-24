package com.app.g_optics.ui.history

import com.app.g_optics.core.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.g_optics.models.history.HistoryRequest
import com.app.g_optics.models.history.HistoryResponse
import com.app.g_optics.repositories.history.HistoryRepository


class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
    private val _historyResult = MutableLiveData<Result<HistoryResponse>>()
    val historyResult: LiveData<Result<HistoryResponse>> = _historyResult

    fun saveHistory(request: HistoryRequest) {
        repository.saveHistory(request) { result ->
            _historyResult.value = result
        }
    }
}