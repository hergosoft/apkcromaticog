package com.app.g_optics.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.g_optics.core.Result
import com.app.g_optics.models.login.LoginRequest
import com.app.g_optics.models.login.LoginResponse
import com.app.g_optics.repositories.login.LoginRepository

// ViewModel para gestionar el login
class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    // MutableLiveData que contiene el resultado del login
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()

    // LiveData que será observado en la actividad
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    // Método para iniciar el proceso de login con el repositorio
    fun login(usuario: String, pass: String) {
        val request = LoginRequest(usuario, pass) // Crea la solicitud de login
        loginRepository.login(request) { result ->
            _loginResult.value = result // Actualiza el resultado del login en el MutableLiveData
        }
    }
}
