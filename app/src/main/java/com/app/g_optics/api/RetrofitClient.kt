package com.app.g_optics.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Objeto singleton que crea una instancia de Retrofit para interactuar con la API
object RetrofitClient {

    // URL base de la API
    private const val BASE_URL = "http://168.243.80.154:1050/api/v1/"

    // Instancia de ApiService que se crea de forma perezosa (lazy), solo cuando se accede a ella
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Establece la URL base
            .addConverterFactory(GsonConverterFactory.create()) // Usa Gson para convertir las respuestas JSON a objetos Kotlin
            .build() // Construye la instancia de Retrofit
            .create(ApiService::class.java) // Crea la implementación de ApiService
    }
}
