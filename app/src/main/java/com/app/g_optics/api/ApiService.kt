package com.app.g_optics.api

import com.app.g_optics.models.history.HistoryRequest
import com.app.g_optics.models.history.HistoryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.app.g_optics.models.login.LoginRequest
import com.app.g_optics.models.login.LoginResponse

// Interface que define los métodos para interactuar con la API usando Retrofit
interface ApiService {

    // Método para iniciar sesión, envía un objeto LoginRequest al endpoint /auth/login
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // Método para guardar un historial, envía un objeto HistoryRequest al endpoint /historyclient/save
    @POST("historyclient/save")
    fun saveHistory(@Body historyRequest: HistoryRequest): Call<HistoryResponse>
}
