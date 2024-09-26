package com.app.g_optics.repositories.login
import com.app.g_optics.api.RetrofitClient
import com.app.g_optics.models.login.LoginRequest
import com.app.g_optics.models.login.LoginResponse
import com.app.g_optics.core.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class LoginRepository {
    fun login(request: LoginRequest, callback: (Result<LoginResponse>) -> Unit) {
        RetrofitClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(Result.Success(it)) // Retorna éxito
                    } ?: callback(Result.Error("Respuesta vacía"))
                } else {
                    callback(Result.Error("Error: ${response.message()}")) // Retorna error
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(Result.Error("Error: ${t.message}")) // Maneja la falla
            }
        })
    }
}