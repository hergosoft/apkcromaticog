package com.app.g_optics.models.login

// Data class que representa la respuesta del servidor al hacer login. Contiene un mensaje y el nombre del usuario (si existe)
data class LoginResponse(val message: String, val nombreUsuario: String?)
