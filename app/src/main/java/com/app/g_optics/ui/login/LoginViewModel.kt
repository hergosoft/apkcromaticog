package com.app.g_optics.ui.login
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.g_optics.core.Result
import com.app.g_optics.models.login.LoginRequest
import com.app.g_optics.models.login.LoginResponse
import com.app.g_optics.repositories.login.LoginRepository
class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel()     {
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult
    fun login(usuario: String, pass: String) {
        val request = LoginRequest(usuario, pass)
        loginRepository.login(request) { result ->
            _loginResult.value = result
        }
    }
}