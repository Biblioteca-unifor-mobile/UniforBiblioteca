package com.example.uniforbiblioteca.ui

import com.example.uniforbiblioteca.dataclass.LivroCardData
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.dialog.DialogConfirmarPedido
import com.example.uniforbiblioteca.rvadapter.LivrosConfirmacaoAdapter
import com.example.uniforbiblioteca.R

class DialogWarningConfirmar : DialogFragment() {

    private lateinit var textViewWarning: TextView
    private lateinit var recyclerViewConfirmacao: RecyclerView
    private lateinit var buttonReservar: Button
    private lateinit var buttonConfirmar: Button
    private lateinit var buttonCancelar: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialot_confirmacao_warning, null)

        // Bind das views
        textViewWarning = view.findViewById(R.id.textView65)
        recyclerViewConfirmacao = view.findViewById(R.id.confirmcao_rv)
        buttonReservar = view.findViewById(R.id.warn_reservar_btn)
        buttonConfirmar = view.findViewById(R.id.warn_confirmar_btn)
        buttonCancelar = view.findViewById(R.id.warn_cancelar_btn)

        // Listeners
        buttonReservar.setOnClickListener {
            val dialog = DialogConfirmarPedido.newInstance(reserva = true)
            dialog.show(parentFragmentManager, "Confirmar")
            dismiss()
        }
        buttonConfirmar.setOnClickListener {
            val dialog = DialogConfirmarPedido.newInstance(reserva = false)
            dialog.show(parentFragmentManager, "Confirmar")
            dismiss()
        }
        buttonCancelar.setOnClickListener {
            dismiss()
        }


        val livros = listOf(
            LivroCardData("1", "Livro 1", "Autor 1", "5 dias atrás", "https://placehold.co/200x300/png","Indisponivel"),
            LivroCardData("2", "Livro 2", "Autor 2", "8 dias atrás", "https://placehold.co/200x300/png",),
            LivroCardData("3", "Livro 3", "Autor 3", "8 dias atrás", "https://placehold.co/200x300/png", "Indisponivel"),
        )

        val adapter = LivrosConfirmacaoAdapter(livros)

        recyclerViewConfirmacao.adapter = adapter

        recyclerViewConfirmacao.layoutManager = LinearLayoutManager(requireContext())



        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

}
