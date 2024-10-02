package com.app.g_optics.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.g_optics.repositories.login.LoginRepository

// Fábrica de ViewModel para crear instancias de LoginViewModel con LoginRepository
class LoginViewModelFactory(private val loginRepository: LoginRepository) : ViewModelProvider.Factory {

    // Método que crea el ViewModel si la clase solicitada es LoginViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(loginRepository) as T // Retorna una instancia de LoginViewModel
        }
        throw IllegalArgumentException("Unknown ViewModel class") // Lanza excepción si no es la clase esperada
    }
}
