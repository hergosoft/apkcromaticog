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

// Actividad de login donde el usuario ingresa sus credenciales
class LoginActivity : AppCompatActivity() {

    // Enlace a la vista con ViewBinding
    private lateinit var binding: ActivityMainBinding

    // Se obtiene el ViewModel de Login usando la fábrica
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(LoginRepository())
    }

    // Método que se ejecuta al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Asocia la vista

        // Accion para el botón de login
        binding.loginButton.setOnClickListener {
            if (isInternetAvailable(this)) { // Comprueba si hay conexión a Internet
                if (checkFields()) { // Comprueba si los campos no están vacíos
                    val username = binding.username.text.toString()
                    val password = binding.password.text.toString()
                    loginViewModel.login(username, password) // Inicia el proceso de login
                }
            } else {
                disconnectDialog() // Muestra diálogo de desconexión
            }
        }

        // Observa los cambios en el resultado del login
        observeLoginResult()
    }

    // Método que comprueba si los campos de usuario y contraseña están completos
    private fun checkFields(): Boolean {
        val username = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()

        return if (username.isEmpty() || password.isEmpty()) {
            binding.textView5.text = "Por favor, complete todos los campos." // Mensaje de error
            false
        } else {
            binding.textView5.text = ""
            true // Los campos están completos
        }
    }

    // Observa los resultados del proceso de login
    private fun observeLoginResult() {
        loginViewModel.loginResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data.message == "Login exitoso") {
                        welcomeDialog(result.data.nombreUsuario) // Si el login es exitoso, muestra un diálogo de bienvenida
                    } else {
                        incorrectDialog() // Si falla el login, muestra un diálogo de error
                    }
                }
                is Result.Error -> {
                    incorrectDialog() // Muestra un diálogo de error en caso de fallo
                }
            }
        }
    }

    // Muestra un diálogo de bienvenida cuando el login es exitoso
    private fun welcomeDialog(nombreUsuario: String?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // Elimina el título del diálogo
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Hace transparente el fondo del diálogo
        dialog.setContentView(R.layout.dialog_welcome) // Establece el layout del diálogo

        dialog.window?.setDimAmount(0f) // Evita el oscurecimiento de fondo

        // Muestra el diálogo
        dialog.show()

        // Usa un Handler para redirigir a otra actividad tras 2 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            // Redirige a HystoryFormActivity y pasa el nombre del usuario
            val intent = Intent(this, HystoryFormActivity::class.java)
            intent.putExtra("nombreUsuario", nombreUsuario)
            startActivity(intent)

            // Cierra la actividad actual después de un pequeño retraso
            Handler(Looper.getMainLooper()).postDelayed({
                finish() // Finaliza la actividad actual
                dialog.dismiss() // Cierra el diálogo
            }, 100)

        }, 1000) // 1 segundo de retraso antes de redirigir
    }

    // Muestra un diálogo cuando el login falla
    private fun incorrectDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // Elimina el título del diálogo
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Hace transparente el fondo del diálogo
        dialog.setContentView(R.layout.dialog_incorrect) // Establece el layout del diálogo

        // Oculta el diálogo cuando se presiona el botón "ocultar"
        val ocultar = dialog.findViewById<Button>(R.id.ocultar)
        ocultar.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show() // Muestra el diálogo
    }

    // Muestra un diálogo cuando no hay conexión a Internet
    private fun disconnectDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // Elimina el título del diálogo
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Hace transparente el fondo del diálogo
        dialog.setContentView(R.layout.dialog_disconnect) // Establece el layout del diálogo

        // Oculta el diálogo cuando se presiona el botón "ocultar"
        val ocultar = dialog.findViewById<Button>(R.id.ocultar)
        ocultar.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show() // Muestra el diálogo
    }
}
