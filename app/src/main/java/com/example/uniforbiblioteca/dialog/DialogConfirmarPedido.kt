package com.example.uniforbiblioteca.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.api.CartAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.CartCheckoutRequest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class DialogConfirmarPedido : DialogFragment() {

    private lateinit var messageTextView: TextView
    private lateinit var confirmarButton: Button
    private lateinit var cancelarButton: Button

    private val cartAPI by lazy {
        RetrofitClient.create(context).create(CartAPI::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val reserva = arguments?.getBoolean(ARG_RESERVA, false) ?: false

        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.dialog_confirmar_pedido, null)

        messageTextView = view.findViewById(R.id.message)
        confirmarButton = view.findViewById(R.id.confirmar_pedido)
        cancelarButton = view.findViewById(R.id.cancelar_pedido)

        // Calcular data limite (ex: 15 dias)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 15)
        
        val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        val dataDisplay = displayDateFormat.format(calendar.time)

        val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        apiDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dataLimiteApi = apiDateFormat.format(calendar.time)

        if (reserva) {
            messageTextView.text = "Você entrou na fila de espera! Avisaremos quando suas reservas estiverem disponíveis."
        } else {
            messageTextView.text = "Você terá até dia $dataDisplay para buscar seus livros!"
        }

        confirmarButton.setOnClickListener {
            if (reserva) {
                // Lógica de reserva (se necessário implementar aqui, mas geralmente é por livro)
                dismiss()
            } else {
                realizarCheckout(dataLimiteApi)
            }
        }

        cancelarButton.setOnClickListener {
            dismiss()
        }

        builder.setView(view)

        return builder.create()
    }

    private fun realizarCheckout(dataLimite: String) {
        lifecycleScope.launch {
            try {
                val request = CartCheckoutRequest(dataLimite)
                val response = cartAPI.checkout(request)
                
                Toast.makeText(context, response.message ?: "Empréstimo confirmado!", Toast.LENGTH_LONG).show()
                
                // Fecha o dialog e retorna resultado para quem chamou ou navega
                // Como estamos em um DialogFragment, podemos tentar acessar o fragmento pai ou activity
                // Uma abordagem simples é fechar e deixar o usuário navegar manualmente, ou forçar voltar
                dismiss()
                
                // Tentativa de voltar da tela de Cesta (que deve estar atrás desse dialog e do Warning)
                // Se o Warning também for dismiss(), voltamos para Cesta.
                // Podemos usar setFragmentResult para notificar CestaFragment
                parentFragmentManager.setFragmentResult("checkout_key", Bundle().apply {
                    putBoolean("success", true)
                })
                
            } catch (e: Exception) {
                Toast.makeText(context, "Erro ao confirmar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val ARG_RESERVA = "arg_reserva"

        fun newInstance(reserva: Boolean): DialogConfirmarPedido {
            val dialog = DialogConfirmarPedido()
            val args = Bundle()
            args.putBoolean(ARG_RESERVA, reserva)
            dialog.arguments = args
            return dialog
        }
    }
}
