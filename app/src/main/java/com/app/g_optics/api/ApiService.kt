package com.app.g_optics.api

import com.app.g_optics.models.history.HistoryRequest
import com.app.g_optics.models.history.HistoryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.app.g_optics.models.login.LoginRequest
import com.app.g_optics.models.login.LoginResponse
interface ApiService {
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("historyclient/save")
    fun saveHistory(@Body historyRequest: HistoryRequest): Call<HistoryResponse>
}