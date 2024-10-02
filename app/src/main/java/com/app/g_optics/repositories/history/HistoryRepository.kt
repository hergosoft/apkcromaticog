package com.app.g_optics.repositories.history

import com.app.g_optics.api.ApiService
import com.app.g_optics.api.RetrofitClient
import com.app.g_optics.core.Result
import com.app.g_optics.models.history.HistoryRequest
import com.app.g_optics.models.history.HistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Clase encargada de gestionar las solicitudes de historial al servidor
class HistoryRepository {

    // Método para guardar un historial, se recibe una solicitud y un callback para procesar el resultado
    fun saveHistory(request: HistoryRequest, callback: (Result<HistoryResponse>) -> Unit) {

        // Se realiza la llamada a la API usando Retrofit
        RetrofitClient.instance.saveHistory(request).enqueue(object : Callback<HistoryResponse> {

            // Método que se ejecuta cuando se recibe una respuesta del servidor
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {

                // Si la respuesta es exitosa (código HTTP 200), retorna éxito con el cuerpo de la respuesta
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(Result.Success(it)) // Llama al callback con el resultado exitoso
                    } ?: callback(Result.Error("Respuesta vacía")) // Si el cuerpo está vacío, retorna error
                } else {
                    // Si la respuesta no es exitosa, retorna un error con el mensaje correspondiente
                    callback(Result.Error("Error: ${response.message()}"))
                }
            }

            // Método que se ejecuta si la solicitud falla (problema de red, timeout, etc.)
            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                // Llama al callback con un mensaje de error si ocurre una excepción
                callback(Result.Error("Error: ${t.message}"))
            }
        })
    }
}
