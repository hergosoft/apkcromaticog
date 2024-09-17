package com.app.g_optics

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import com.app.g_optics.databinding.ActivityMainBinding

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

                if (username == "user" && password == "1234") {
                    welcome_dialog()
                } else {
                    incorrect_dialog()
                }
            }
        }
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

    private fun welcome_dialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_welcome)

        val siguiente = dialog.findViewById<Button>(R.id.siguiente)

        siguiente.setOnClickListener {
            dialog.dismiss()
            // Redirige al login (MainActivity)
            val intent = Intent(this, HystoryFormActivity::class.java)
            startActivity(intent)
            finish()
        }

        dialog.show()
    }

    private fun incorrect_dialog(){
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