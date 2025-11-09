package com.example.uniforbiblioteca.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.uniforbiblioteca.R
import com.google.android.material.slider.RangeSlider
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HistoricoFilterDialogFragment : DialogFragment() {

    lateinit var alugadosCheck: CheckBox
    lateinit var reservadosCheck: CheckBox
    lateinit var devolvidosCheck: CheckBox
    lateinit var todosCheck: CheckBox
    lateinit var pesquisarHistoricoButton: Button

    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val startDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -200) }
    private val today = Calendar.getInstance()
    private val totalDays = ((today.timeInMillis - startDate.timeInMillis) / (1000 * 60 * 60 * 24)).toFloat()

    private lateinit var slider: RangeSlider
    private lateinit var startText: EditText
    private lateinit var endText: EditText

    private val startTextWatcher = createTextWatcher(::handleStartTextChanged)
    private val endTextWatcher = createTextWatcher(::handleEndTextChanged)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_filter_historico, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        slider = view.findViewById(R.id.dateRangeSlider)
        startText = view.findViewById(R.id.startDateText)
        endText = view.findViewById(R.id.endDateText)

        alugadosCheck = view.findViewById(R.id.filtroAlugados)
        reservadosCheck = view.findViewById(R.id.filtroReservados)
        devolvidosCheck = view.findViewById(R.id.filtroDevolvidos)
        todosCheck = view.findViewById(R.id.filtroTodos)
        pesquisarHistoricoButton = view.findViewById(R.id.pesquisarHistoricoButton)

        alugadosCheck.isChecked = true
        todosCheck.isChecked = false

        setupCheckBoxesLogic()

        slider.valueFrom = 0f
        slider.valueTo = totalDays
        slider.stepSize = 1f
        slider.values = listOf(totalDays - 50f, totalDays)

        setupSliderToInputSync()
        setupInputToSliderSync()
        updateInputTexts(slider.values[0], slider.values[1])

        setupPesquisarButtonListener()
    }

    private fun setupPesquisarButtonListener() {
        pesquisarHistoricoButton.setOnClickListener {
            dismiss()
        }
    }

    private fun setupCheckBoxesLogic() {
        val individualChecks = listOf(alugadosCheck, reservadosCheck, devolvidosCheck)

        todosCheck.setOnCheckedChangeListener { _, isChecked ->
            alugadosCheck.isChecked = isChecked
            reservadosCheck.isChecked = isChecked
            devolvidosCheck.isChecked = isChecked
        }

        val individualListener = { _: View, isChecked: Boolean ->
            if (!isChecked) {
                todosCheck.setOnCheckedChangeListener(null)
                todosCheck.isChecked = false
                setupTodosCheckListener()
            } else {
                val allChecked = individualChecks.all { it.isChecked }
                if (allChecked) {
                    todosCheck.setOnCheckedChangeListener(null)
                    todosCheck.isChecked = true
                    setupTodosCheckListener()
                }
            }
        }



        alugadosCheck.setOnCheckedChangeListener(individualListener)
        reservadosCheck.setOnCheckedChangeListener(individualListener)
        devolvidosCheck.setOnCheckedChangeListener(individualListener)

        setupTodosCheckListener()

        val allInitiallyChecked = individualChecks.all { it.isChecked }
        if (allInitiallyChecked) {
            todosCheck.isChecked = true
        }
    }

    private fun setupTodosCheckListener() {
        todosCheck.setOnCheckedChangeListener { _, isChecked ->
            alugadosCheck.isChecked = isChecked
            reservadosCheck.isChecked = isChecked
            devolvidosCheck.isChecked = isChecked
        }
    }

    private fun createTextWatcher(callback: (Editable?) -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                callback(s)
            }
        }
    }

    private fun handleStartTextChanged(editable: Editable?) {
        val text = editable.toString()
        try {
            val date = sdf.parse(text)
            if (date != null) {
                val newSliderValue = getSliderValueFromDate(date)
                val endValue = slider.values[1]

                if (newSliderValue > endValue) {
                    startText.error = "A data inicial não pode ser maior que a final."
                    return
                }

                startText.error = null
                slider.values = listOf(newSliderValue, endValue)

            } else {
                startText.error = "Formato de data inválido (dd/MM/yyyy)."
            }
        } catch (e: ParseException) {
            startText.error = "Formato de data inválido (dd/MM/yyyy)."
        }
    }

    private fun handleEndTextChanged(editable: Editable?) {
        val text = editable.toString()
        try {
            val date = sdf.parse(text)
            if (date != null) {
                val newSliderValue = getSliderValueFromDate(date)
                val startValue = slider.values[0]

                if (newSliderValue < startValue) {
                    endText.error = "A data final não pode ser menor que a inicial."
                    return
                }

                endText.error = null
                slider.values = listOf(startValue, newSliderValue)

            } else {
                endText.error = "Formato de data inválido (dd/MM/yyyy)."
            }
        } catch (e: ParseException) {
            endText.error = "Formato de data inválido (dd/MM/yyyy)."
        }
    }

    private fun getDateFromSliderValue(value: Float): Date {
        val cal = startDate.clone() as Calendar
        cal.add(Calendar.DAY_OF_YEAR, value.toInt())
        return cal.time
    }

    private fun getSliderValueFromDate(date: Date): Float {
        val diff = date.time - startDate.timeInMillis
        return (diff / (1000 * 60 * 60 * 24)).toFloat()
    }

    private fun updateInputTexts(startValue: Float, endValue: Float) {
        val startDateSelected = getDateFromSliderValue(startValue)
        val endDateSelected = getDateFromSliderValue(endValue)

        startText.removeTextChangedListener(startTextWatcher)
        endText.removeTextChangedListener(endTextWatcher)

        startText.setText(sdf.format(startDateSelected))
        endText.setText(sdf.format(endDateSelected))

        startText.addTextChangedListener(startTextWatcher)
        endText.addTextChangedListener(endTextWatcher)
    }

    private fun setupSliderToInputSync() {
        slider.addOnChangeListener { slider, _, _ ->
            updateInputTexts(slider.values[0], slider.values[1])
        }
    }

    private fun setupInputToSliderSync() {
        startText.addTextChangedListener(startTextWatcher)
        endText.addTextChangedListener(endTextWatcher)
    }
}