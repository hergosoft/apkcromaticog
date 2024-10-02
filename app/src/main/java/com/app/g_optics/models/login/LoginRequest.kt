package com.app.g_optics.models.login

// Data class que representa la solicitud de login, con los campos 'usuario' y 'pass' que se enviarán en la petición
data class LoginRequest(val usuario: String, val pass: String)
