package com.example.uniforbiblioteca.ui

import com.example.uniforbiblioteca.dataclass.LivroCardData
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.api.CartAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.CartCheckoutRequest
import com.example.uniforbiblioteca.rvadapter.LivrosConfirmacaoAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class DialogWarningConfirmar : DialogFragment() {

    private lateinit var textViewWarning: TextView
    private lateinit var recyclerViewConfirmacao: RecyclerView
    private lateinit var buttonReservar: Button
    private lateinit var buttonConfirmar: Button
    private lateinit var buttonCancelar: Button

    private val cartAPI by lazy {
        RetrofitClient.create(context).create(CartAPI::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialot_confirmacao_warning, null)

        // Bind das views
        textViewWarning = view.findViewById(R.id.textView65)
        recyclerViewConfirmacao = view.findViewById(R.id.confirmcao_rv)
        buttonReservar = view.findViewById(R.id.warn_reservar_btn)
        buttonConfirmar = view.findViewById(R.id.warn_confirmar_btn)
        buttonCancelar = view.findViewById(R.id.warn_cancelar_btn)

        recyclerViewConfirmacao.layoutManager = LinearLayoutManager(requireContext())
        
        // Carrega os itens reais da cesta para confirmação
        carregarItensCesta()

        // Listeners
        buttonReservar.setOnClickListener {
            // Lógica de reserva futura (quando a API suportar ou por item)
            // Por enquanto, apenas fecha o diálogo
            dismiss()
        }
        
        buttonConfirmar.setOnClickListener {
            realizarCheckout()
        }
        
        buttonCancelar.setOnClickListener {
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    private fun carregarItensCesta() {
        lifecycleScope.launch {
            try {
                val items = cartAPI.getCart()
                val livrosConfirmacao = items.map { item ->
                    LivroCardData(
                        id = item.bookId ?: "0",
                        titulo = item.book?.titulo ?: "Sem Título",
                        autor = item.book?.autor ?: "Sem Autor",
                        tempo = "Adicionado recentemente", 
                        image = item.book?.imageUrl ?: "",
                        status = "Na Cesta"
                    )
                }
                val adapter = LivrosConfirmacaoAdapter(livrosConfirmacao)
                recyclerViewConfirmacao.adapter = adapter
            } catch (e: Exception) {
                Log.e("DIALOG_WARNING", "Erro ao carregar itens para confirmação", e)
            }
        }
    }

    private fun realizarCheckout() {
        // Data limite padrão (ex: 15 dias)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 15)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dataLimite = dateFormat.format(calendar.time)

        val request = CartCheckoutRequest(dataLimite)

        lifecycleScope.launch {
            try {
                val response = cartAPI.checkout(request)
                Toast.makeText(context, response.message ?: "Empréstimo realizado!", Toast.LENGTH_LONG).show()
                
                // Atualiza a UI da CestaFragment se possível (via Fragment Result API)
                parentFragmentManager.setFragmentResult("checkout_key", Bundle().apply {
                    putBoolean("success", true)
                })

                dismiss()
            } catch (e: Exception) {
                Log.e("DIALOG_WARNING", "Erro no checkout", e)
                Toast.makeText(context, "Erro ao confirmar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
