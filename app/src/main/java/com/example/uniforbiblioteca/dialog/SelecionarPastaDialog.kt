package com.example.uniforbiblioteca.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.PastaCardData
import com.example.uniforbiblioteca.rvadapter.SelecionarPastaAdapter

class SelecionarPastaDialog(context: Context) : Dialog(context) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnConfirmar: Button
    private lateinit var btnCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_select_pasta)

        recyclerView = findViewById(R.id.select_rv)
        btnConfirmar = findViewById(R.id.confirmar_select)
        btnCancelar = findViewById(R.id.cancelar_select)

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Pastas de placeholders
        val pastas = listOf(
            PastaCardData(
                1,
                "Estrutura de Dados",
                "Ultima Modificação: 2025-10-08",
                "https://placehold.co/200x300/png"
            ),
            PastaCardData(
                2,
                "Java",
                "Ultima Modificação: 2025-10-07",
                "https://placehold.co/200x300/png"
            ),
            PastaCardData(
                3,
                "Redes",
                "Ultima Modificação: 2025-10-06",
                "https://placehold.co/200x300/png"
            ),
            PastaCardData(
                4,
                "Integração",
                "Ultima Modificação: 2025-10-05",
                "https://placehold.co/200x300/png"
            ),
        )

        // Adapter
        val adapter = SelecionarPastaAdapter(pastas)

        recyclerView.adapter = adapter

        setupListeners()
    }

    private fun setupListeners() {
        btnConfirmar.setOnClickListener {
            onConfirmarClick()
        }

        btnCancelar.setOnClickListener {
            onCancelarClick()
        }
    }

    private fun onConfirmarClick() {
        dismiss()
    }

    private fun onCancelarClick() {
        dismiss()
    }
}