package com.app.g_optics

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.g_optics.databinding.ActivityHystoryFormBinding
import java.util.*

class HystoryFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHystoryFormBinding
    private var selectedMonth: String? = null
    // Bandera para controlar el estado de visibilidad de los contenedores
    private var isContentVisible1 = false
    private var isContentVisible2 = false
    private var isContentVisible3 = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupListeners()
        setupFilters()

    }

    private fun setupBinding() {
        // Inicializa el binding correctamente
        binding = ActivityHystoryFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



    private fun setupFilters() {
        // Configurar filtro para nombre completo (solo letras y espacios)
        binding.txtnombre.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.matches("[a-zA-Z\\s]+".toRegex())) null else ""
        })

        // Configurar filtro para nombre completo (solo letras y espacios)
        binding.txtocupacion.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.matches("[a-zA-Z\\s]+".toRegex())) null else ""
        })

    }

    private fun setupListeners() {
        // Configura el listener del botón logout
        binding.logoutbtn.setOnClickListener {
            navigateToLogin()
        }

        // Configura el listener para el spinner de selección de mes
        binding.spUltimoMes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                handleMonthSelection(parent, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedMonth = null
            }
        }

        // Inicializa el click para abrir el calendario
        binding.calendarEditText.setOnClickListener {
            showDatePicker()
        }

        // Referencias a los CheckBox
        val checkBoxSi = findViewById<CheckBox>(R.id.checklentessi)
        val checkBoxNo = findViewById<CheckBox>(R.id.checklentesno)

        // Listener para "Sí"
        checkBoxSi.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBoxNo.isEnabled = false // Deshabilita el "No" cuando "Sí" está marcado
            } else {
                checkBoxNo.isEnabled = true // Habilita el "No" cuando "Sí" está desmarcado
            }
        }

        // Listener para "No"
        checkBoxNo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBoxSi.isEnabled = false // Deshabilita el "Sí" cuando "No" está marcado
            } else {
                checkBoxSi.isEnabled = true // Habilita el "Sí" cuando "No" está desmarcado
            }
        }


    }

    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Opcional: Termina la actividad actual para que no quede en el historial
    }

    private fun handleMonthSelection(parent: AdapterView<*>?, position: Int) {
        selectedMonth = parent?.getItemAtPosition(position).toString()
        saveMonthSelection()
    }

    private fun saveMonthSelection() {
        if (selectedMonth != null) {
            // Guardar el mes seleccionado en alguna base de datos o preferencias
            Toast.makeText(this, "Mes guardado: $selectedMonth", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se ha seleccionado un mes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Aplicar el tema personalizado al DatePickerDialog
        val datePicker = DatePickerDialog(
            this, R.style.CustomDatePickerDialog, // Aplicación del estilo personalizado
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.calendarEditText.setText(selectedDate)

                val age = calculateAge(selectedYear, selectedMonth, selectedDay)
                binding.textedad.text = "$age años"
            }, year, month, day
        )

        datePicker.show()
    }
    private fun handleDateSelection(year: Int, month: Int, day: Int) {
        // Formatear la fecha seleccionada
        val selectedDate = "$day/${month + 1}/$year"
        binding.calendarEditText.setText(selectedDate)

        // Calcular la edad
        val age = calculateAge(year, month, day)
        binding.textedad.text = "$age años"
    }

    private fun calculateAge(year: Int, month: Int, day: Int): Int {
        val today = Calendar.getInstance()
        val birthDate = Calendar.getInstance()
        birthDate.set(year, month, day)

        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

        // Verificar si aún no ha cumplido años en el año actual
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }
}
