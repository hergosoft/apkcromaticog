package com.app.g_optics

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.g_optics.databinding.ActivityMainBinding
import com.app.g_optics.login.LoginRequest
import com.app.g_optics.login.LoginResponse
import com.app.g_optics.login.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            if (checkFields()) {
                val username = binding.username.text.toString()
                val password = binding.password.text.toString()
                login(username, password)
            }
        }
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

    private fun login(usuario: String, pass: String) {
        val loginRequest = LoginRequest(usuario, pass)

        RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Verifica si la respuesta es exitosa
                if (response.isSuccessful) {
                    val message = response.body()?.message
                    if (message == "Login exitoso") {
                        welcome_dialog()  // Mostrar el diálogo de bienvenida
                    } else {
                        incorrect_dialog()  // Mensaje de usuario o contraseña incorrectos
                    }
                } else {
                    // Aquí manejamos el caso donde la respuesta no es exitosa
                    val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                    if (errorMessage.contains("Usuario o contraseña incorrectos")) {
                        incorrect_dialog()  // Mostrar diálogo de error
                    } else {
                        Toast.makeText(this@MainActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun welcome_dialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_welcome)

        val siguiente = dialog.findViewById<Button>(R.id.siguiente)

        siguiente.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, HystoryFormActivity::class.java)
            startActivity(intent)
            finish()
        }

        dialog.show()
    }

    private fun incorrect_dialog() {
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
