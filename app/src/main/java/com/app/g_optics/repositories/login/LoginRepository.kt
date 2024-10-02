package com.app.g_optics.repositories.login

import android.util.Log
import com.app.g_optics.api.RetrofitClient
import com.app.g_optics.models.login.LoginRequest
import com.app.g_optics.models.login.LoginResponse
import com.app.g_optics.core.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Repositorio para gestionar las llamadas relacionadas con el login
class LoginRepository {

    // Método que realiza el login enviando un LoginRequest y maneja el callback con el resultado
    fun login(request: LoginRequest, callback: (Result<LoginResponse>) -> Unit) {
        RetrofitClient.instance.login(request).enqueue(object : Callback<LoginResponse> {

            // Se ejecuta cuando se obtiene una respuesta de la API
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, retorna el cuerpo de la respuesta
                    response.body()?.let {
                        callback(Result.Success(it)) // Llama el callback con éxito
                    } ?: callback(Result.Error("Respuesta vacía")) // Si el cuerpo es nulo, retorna error
                } else {
                    // Si la respuesta no es exitosa, retorna un mensaje de error
                    callback(Result.Error("Error: ${response.message()}"))
                }
            }

            // Se ejecuta cuando la llamada a la API falla
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("API_CALL", "Error: ${t.message}") // Registra el error en los logs
                callback(Result.Error("Error: ${t.message}")) // Llama el callback con un mensaje de error
            }
        })
    }
}
