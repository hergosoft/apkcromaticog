package com.app.g_optics.repositories.history

import com.app.g_optics.api.ApiService
import com.app.g_optics.api.RetrofitClient
import com.app.g_optics.core.Result
import com.app.g_optics.models.history.HistoryRequest
import com.app.g_optics.models.history.HistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryRepository {
    fun saveHistory(request: HistoryRequest, callback: (Result<HistoryResponse>) -> Unit) {
        RetrofitClient.instance.saveHistory(request).enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(Result.Success(it)) // Retorna éxito
                    } ?: callback(Result.Error("Respuesta vacía"))
                } else {
                    callback(Result.Error("Error: ${response.message()}")) // Retorna error
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                callback(Result.Error("Error: ${t.message}")) // Maneja la falla
            }
        })
    }
}