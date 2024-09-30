
package com.app.g_optics.ui.login


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.g_optics.ui.history.HystoryFormActivity
import com.app.g_optics.R
import com.app.g_optics.core.Result
import com.app.g_optics.databinding.ActivityMainBinding
import com.app.g_optics.repositories.login.LoginRepository
import com.app.g_optics.utils.isInternetAvailable

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
            if (isInternetAvailable(this)) {
                if (checkFields()) {
                    val username = binding.username.text.toString()
                    val password = binding.password.text.toString()
                    loginViewModel.login(username, password)
                }
            } else {
               disconnectDialog()
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
                        welcomeDialog(result.data.nombreUsuario) // Pasamos el nombre del usuario aquí
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

    private fun welcomeDialog(nombreUsuario: String?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_welcome)

        dialog.window?.setDimAmount(0f)

        // Mostrar el diálogo
        dialog.show()

        // Usamos un Handler para programar la redirección tras 2 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            // Redirige a HystoryFormActivity y pasa el nombre de usuario
            val intent = Intent(this, HystoryFormActivity::class.java)
            intent.putExtra("nombreUsuario", nombreUsuario) // Pasamos el nombre del usuario al siguiente Activity
            startActivity(intent)

            // En lugar de llamar al finish() inmediatamente, lo hacemos después de un pequeño delay
            Handler(Looper.getMainLooper()).postDelayed({
                // Cerramos la actividad actual para evitar que el diálogo permanezca montado
                finish()
                // Ahora cerramos el diálogo en segundo plano
                dialog.dismiss()
            }, 100) // Un retraso de 100 ms para asegurarnos de que la nueva actividad ya se haya mostrado

        }, 1000) // 2000 ms = 2 segundos
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

    private fun disconnectDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_disconnect)

        val ocultar = dialog.findViewById<Button>(R.id.ocultar)
        ocultar.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
