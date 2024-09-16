package com.app.g_optics

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.g_optics.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // El binding está bien declarado aquí
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            if (checkFields()) {
                val username = binding.username.text.toString()
                val password = binding.password.text.toString()

                if (username == "user" && password == "1234") {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        0

    }

    // Función para verificar si los campos están vacíos
    private fun checkFields(): Boolean {
        val username = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()

        return if (username.isEmpty() || password.isEmpty()) {
            // Mostrar mensaje de error si alguno de los campos está vacío
            binding.textView5.text = "Por favor, complete todos los campos."
            false  // Detener el flujo
        } else {
            // Limpiar el mensaje de error si ambos campos están completos
            binding.textView5.text = ""
            true  // Continuar el flujo
        }
    }
}