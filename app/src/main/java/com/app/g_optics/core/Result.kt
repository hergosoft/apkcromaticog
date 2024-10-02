package com.app.g_optics.core

// Clase sellada (sealed class) para modelar el resultado de una operación, que puede ser éxito o error
sealed class Result<out T> {

    // Representa el estado de éxito con un dato de tipo genérico T
    data class Success<out T>(val data: T) : Result<T>()

    // Representa el estado de error con un mensaje
    data class Error(val message: String) : Result<Nothing>()
}
