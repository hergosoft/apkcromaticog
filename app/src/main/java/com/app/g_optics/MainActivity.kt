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
// Inflando el binding con el layoutInflater
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Configurando el root del layout en setContentView
        setContentView(binding.root)

        // Listener para el botón login
        binding.loginButton.setOnClickListener {
              // Verificar si el clic funciona en Logcat

            // Recupera el texto de username y password
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            // Valida si el login es correcto o incorrecto
            if (username == "user" && password == "1234") {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}