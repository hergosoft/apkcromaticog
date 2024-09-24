package com.app.g_optics.ui.login


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.g_optics.HystoryFormActivity
import com.app.g_optics.R
import com.app.g_optics.core.Result
import com.app.g_optics.databinding.ActivityMainBinding
import com.app.g_optics.repositories.login.LoginRepository

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(LoginRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            if (checkFields()) {
                val username = binding.username.text.toString()
                val password = binding.password.text.toString()
                loginViewModel.login(username, password)
            }
        }

        observeLoginResult()
    }

    private fun checkFields(): Boolean {
        val username = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()

        return if (username.isEmpty() || password.isEmpty()) {
            binding.textView5.text = "Por favor, complete todos los campos."
            false
        } else {
            binding.textView5.text = ""
            true
        }
    }

    private fun observeLoginResult() {
        loginViewModel.loginResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data.message == "Login exitoso") {
                        welcomeDialog()
                    } else {
                        incorrectDialog()
                    }
                }
                is Result.Error -> {
                    incorrectDialog()
                }
            }
        }
    }

    private fun welcomeDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_welcome)

        val siguiente = dialog.findViewById<Button>(R.id.siguiente)
        siguiente.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, HystoryFormActivity::class.java))
            finish()
        }
        dialog.show()
    }

    private fun incorrectDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_incorrect)

        val ocultar = dialog.findViewById<Button>(R.id.ocultar)
        ocultar.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}