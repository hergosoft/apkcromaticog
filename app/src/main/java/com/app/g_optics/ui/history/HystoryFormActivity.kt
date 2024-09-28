package com.app.g_optics.ui.history

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.app.g_optics.R
import com.app.g_optics.core.Result
import com.app.g_optics.databinding.ActivityHystoryFormBinding
import com.app.g_optics.models.history.HistoryRequest
import com.app.g_optics.repositories.history.HistoryRepository
import com.app.g_optics.ui.login.LoginActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class HystoryFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHystoryFormBinding
    private var selectedMonth: String? = null
    private var selectedMedio: String? = null
    private var nombreUsuario: String? = null
    val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)


    private val viewModel: HistoryViewModel by viewModels {
        HistoryViewModelFactory(HistoryRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupListeners()
        setupFilters()
        setupToggleContent()
        // Establecer la fecha actual como un dato en el ViewModel

        nombreUsuario = intent.getStringExtra("nombreUsuario")

        // Usar String.format para incluir el nombre de usuario en la cadena
        binding.trabajador.text = getString(R.string.trabajador_text, nombreUsuario)
        observeHistoryResult()
    }

    private fun observeHistoryResult() {
        viewModel.historyResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    save_dialog()
                }

                is Result.Error -> {

                }
            }
        }
    }

    private fun setupBinding() {
        // Inicializa el binding correctamente
        binding = ActivityHystoryFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    // Configurar el comportamiento de los contenedores desplegables
    private fun setupToggleContent() {
        setupToggleBehavior(binding.toggleContainer1, binding.hiddenContent1, binding.toggleIcon1)
        setupToggleBehavior(binding.toggleContainer2, binding.hiddenContent2, binding.toggleIcon2)
        setupToggleBehavior(binding.toggleContainer3, binding.hiddenContent3, binding.toggleIcon3)
        setupToggleBehavior(binding.toggleContainer4, binding.hiddenContent4, binding.toggleIcon4)
    }

    private fun setupToggleBehavior(
        container: LinearLayout,
        hiddenContent: LinearLayout,
        icon: ImageView
    ) {
        container.setOnClickListener {
            if (hiddenContent.visibility == View.GONE) {
                hiddenContent.visibility = View.VISIBLE
                icon.setImageResource(R.drawable.ic_baseline_expand_less) // Cambiar a "menos"
            } else {
                hiddenContent.visibility = View.GONE
                icon.setImageResource(R.drawable.ic_baseline_expand_more) // Cambiar a "más"
            }
        }
    }


    private fun setupFilters() {
        // Configurar filtro para nombre completo (solo letras y espacios)
        binding.txtnombre.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.matches("[a-zA-Z\\s]+".toRegex())) null else ""
        })

        // Configurar filtro para nombre completo (solo letras y espacios)
        binding.txtocupacion.filters =
            arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
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

        binding.spmedios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                handleMedioSelection(parent, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedMonth = null
            }
        }

        // Inicializa el click para abrir el calendario
        binding.calendarEditText.setOnClickListener {
            showDatePicker()
        }


        //---------------------------------------------------------------------------
        // Referencias a los CheckBox usa lentes
        val checkBoxusaSi = findViewById<CheckBox>(R.id.checklentessi)
        val checkBoxusaNo = findViewById<CheckBox>(R.id.checklentesno)


        // Listener para "Sí"
        checkBoxusaSi.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBoxusaNo.isEnabled = false // Deshabilita el "No" cuando "Sí" está marcado
            } else {
                checkBoxusaNo.isEnabled = true // Habilita el "No" cuando "Sí" está desmarcado
            }
        }

        // Listener para "No"
        checkBoxusaNo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBoxusaSi.isEnabled = false // Deshabilita el "Sí" cuando "No" está marcado
            } else {
                checkBoxusaSi.isEnabled = true // Habilita el "Sí" cuando "No" está desmarcado
            }
        }

        // Referencias a los CheckBox
        val checkBoxciruSi = findViewById<CheckBox>(R.id.checkcirugiasi)
        val checkBoxciruNo = findViewById<CheckBox>(R.id.checkcirugiano)

        // Listener para "Sí"
        checkBoxciruSi.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBoxciruNo.isEnabled = false // Deshabilita el "No" cuando "Sí" está marcado
            } else {
                checkBoxciruNo.isEnabled = true // Habilita el "No" cuando "Sí" está desmarcado
            }
        }

        // Listener para "No"
        checkBoxciruNo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBoxciruSi.isEnabled = false // Deshabilita el "Sí" cuando "No" está marcado
            } else {
                checkBoxciruSi.isEnabled = true // Habilita el "Sí" cuando "No" está desmarcado
            }
        }

        binding.savebutton.setOnClickListener {
            saveHistory()
        }


    }

    //-----------------------FUNCION PARA GUARDAR LOS DATOS EN LA BASE DE DATOS
    private fun saveHistory() {
        // Aquí creas el objeto HistoryRequest
        val request = createHistoryRequest()

        // Solo procede si request no es null (es decir, si todos los campos obligatorios están llenos)
        if (request != null) {
            // Llamamos a la función para guardar el historial
            viewModel.saveHistory(request)
        }
    }

    private fun createHistoryRequest(): HistoryRequest? {
        val edadString = binding.textedad.text.toString()
        val edad = edadString.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
        //-----------------------------------------------CHECKS
        // Referencias a los CheckBox usa lentes
        val checkBoxusaSi = findViewById<CheckBox>(R.id.checklentessi)
        val checkBoxusaNo = findViewById<CheckBox>(R.id.checklentesno)
        // Referencias a los CheckBox cirugia
        val checkBoxciruSi = findViewById<CheckBox>(R.id.checkcirugiasi)
        val checkBoxciruNo = findViewById<CheckBox>(R.id.checkcirugiano)

        if (binding.txtnombre.text.toString().isEmpty() && binding.txttelefono.text.toString()
                .isEmpty() && binding.textedad.text.toString()
                .isEmpty() && binding.txtesfodrx6.text.toString()
                .isEmpty() && binding.txtcilodrx6.text.toString()
                .isEmpty() && binding.txtejeodrx6.text.toString().isEmpty()
            && binding.txtadvodrx6.text.toString().isEmpty()
            && binding.txtaddodrx6.text.toString().isEmpty()
            && binding.txtesfoirx6.text.toString().isEmpty()
            && binding.txtciloirx6.text.toString().isEmpty()
            && binding.txtejeoirx6.text.toString().isEmpty()
            && binding.txtadvoirx6.text.toString().isEmpty()
            && binding.txtalturarx6.text.toString().isEmpty()
            && binding.txtoddnprx6.text.toString().isEmpty()
            && binding.txtoidnprx6.text.toString().isEmpty()
            && binding.txtobservaciones.text.toString().isEmpty()
            && binding.txtemail.text.toString().isEmpty()
        ) {
            // Si los campos obligatorios están vacíos, mostrar el mensaje de error y no proceder
            binding.textcamposvacios.visibility = View.VISIBLE
            return null
            // No se crea ni retorna el objeto HistoryRequest
        } else {
            // Rellena el objeto HistoryRequest con los datos desde los campos del formulario
            return HistoryRequest(
                fecha = currentDate,
                idAgencia = "2",
                paciente = binding.txtnombre.text.toString(),
                edad = edad,
                nit = binding.txtnit.text.toString(),
                ocupacion = binding.txtocupacion.text.toString(),
                telefono = binding.txttelefono.text.toString(),
                direccion = binding.txtdireccion.text.toString(),
                medio = selectedMedio.toString(),
                antecedentes = binding.txtantecedentes.text.toString(),
                ciru = if (checkBoxciruSi.isChecked) "X" else if (checkBoxciruNo.isChecked) "0" else "No especificado",
                obsciru = binding.txtcirugia.text.toString(),
                usa = if (checkBoxusaSi.isChecked) "X" else if (checkBoxusaNo.isChecked) "0" else "No especificado",
                ultChek = selectedMonth.toString(),
                motivo = binding.txtmc.text.toString(),
                rx1OdEsf = binding.txtesfodrx1.text.toString(),
                rx1OdCil = binding.txtcilodrx1.text.toString(),
                rx1OdEje = binding.txtejeodrx1.text.toString(),
                rx1OdAv = binding.txtadvodrx1.text.toString(),
                rx1OdAdd = binding.txtaddOdrx1.text.toString(),
                rx1OdAv1 = binding.txtav1odrx1.text.toString(),
                rx1OiEsf = binding.txtesfoirx1.text.toString(),
                rx1OiCil = binding.txtciloirx1.text.toString(),
                rx1OiEje = binding.txtejeoirx1.text.toString(),
                rx1OiAv = binding.txtadvodrx1.text.toString(),
                rx1OiAdd = binding.txtaddOirx1.text.toString(),
                rx1OiAv1 = binding.txtav1oirx1.text.toString(),
                rx1Add = binding.txtaddrx1.text.toString(),
                rx2OdEsf = binding.txtavscodrx2.text.toString(),
                rx2OdEje = binding.txtavscaorx2.text.toString(),
                rx2OiEsf = binding.txtavscoirx2.text.toString(),
                rx3OdEsf = binding.txtesfodrx3.text.toString(),
                rx3OdCil = binding.txtcilodrx3.text.toString(),
                rx3OdEje = binding.txtejeodrx3.text.toString(),
                rx3OdAv = binding.txtadvodrx3.text.toString(),
                rx3OiEsf = binding.txtesfoirx3.text.toString(),
                rx3OiCil = binding.txtciloirx3.text.toString(),
                rx3OiEje = binding.txtejeoirx3.text.toString(),
                rx3OiAv = binding.txtadvoirx3.text.toString(),
                rx3Add = binding.txtaddrx3.text.toString(),
                rx4OdEsf = binding.txtesfodrx4.text.toString(),
                rx4OdCil = binding.txtcilodrx4.text.toString(),
                rx4OdEje = binding.txtejeodrx4.text.toString(),
                rx4OdAv = binding.txtadvodrx4.text.toString(),
                rx4OiEsf = binding.txtesfoirx4.text.toString(),
                rx4OiCil = binding.txtciloirx4.text.toString(),
                rx4OiEje = binding.txtejeoirx4.text.toString(),
                rx4OiAv = binding.txtadvoirx4.text.toString(),
                rx4Add = binding.txtaddrx4.text.toString(),
                rx5OdEsf = binding.txtesfodrx5.text.toString(),
                rx5OdCil = binding.txtcilodrx5.text.toString(),
                rx5OdEje = binding.txtejeodrx5.text.toString(),
                rx5OdAv = binding.txtadvodrx5.text.toString(),
                rx5OiEsf = binding.txtesfoirx5.text.toString(),
                rx5OiCil = binding.txtciloirx5.text.toString(),
                rx5OiEje = binding.txtejeoirx5.text.toString(),
                rx5OiAv = binding.txtadvoirx5.text.toString(),
                rx5Obs = binding.txtobservacionesrx5.text.toString(),
                rx6OdEsf = binding.txtesfodrx6.text.toString(),
                rx6OdCil = binding.txtcilodrx6.text.toString(),
                rx6OdEje = binding.txtejeodrx6.text.toString(),
                rx6OdAv = binding.txtadvodrx6.text.toString(),
                rx6OdAdd = binding.txtaddodrx6.text.toString(),
                rx6OiEsf = binding.txtesfoirx6.text.toString(),
                rx6OiCil = binding.txtciloirx6.text.toString(),
                rx6OiEje = binding.txtejeoirx6.text.toString(),
                rx6OiAv = binding.txtadvoirx6.text.toString(),
                rx6OiAdd = binding.txtaddoirx6.text.toString(),
                altura = binding.txtalturarx6.text.toString(),
                obs = binding.txtobservaciones.text.toString(),
                optometra = nombreUsuario ?: "Desconocido",
                rx6OdDnp = binding.txtoddnprx6.text.toString(),
                rx6OiDnp = binding.txtoidnprx6.text.toString(),
                email = binding.txtemail.text.toString()
            )
            binding.textcamposvacios.visibility = View.GONE
        }
    }






    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
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

    private fun handleMedioSelection(parent: AdapterView<*>?, position: Int) {
        selectedMedio = parent?.getItemAtPosition(position).toString()
        savemedioSelection()
    }
    private fun savemedioSelection() {
        if (selectedMedio != null) {
            // Guardar el mes seleccionado en alguna base de datos o preferencias
            Toast.makeText(this, "Medio guardado: $selectedMedio", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se ha seleccionado un medio", Toast.LENGTH_SHORT).show()
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

    private fun save_dialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_save)

        dialog.window?.setDimAmount(0f)
        dialog.show()

        // Usamos un Handler para programar la redirección tras 2 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            // Redirige a HystoryFormActivity y pasa el nombre de usuario
            val intent = Intent(this, LoginActivity::class.java)
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
}
