package com.example.uniforbiblioteca.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.Folder
import com.example.uniforbiblioteca.dataclass.PastaCardData
import com.example.uniforbiblioteca.rvadapter.SelecionarPastaAdapter
import com.example.uniforbiblioteca.viewmodel.FolderManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelecionarPastaDialog(context: Context, val onConfirm: suspend (Folder) -> Boolean) : Dialog(context) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnConfirmar: Button
    private lateinit var btnCancelar: Button

    private var selectedFolder: Folder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_select_pasta)

        recyclerView = findViewById(R.id.select_rv)
        btnConfirmar = findViewById(R.id.confirmar_select)
        btnCancelar = findViewById(R.id.cancelar_select)

        recyclerView.layoutManager = LinearLayoutManager(context)


        // Adapter
        val adapter = SelecionarPastaAdapter(FolderManager.folderList) { folder ->
            selectedFolder = folder
        }

        recyclerView.adapter = adapter

        setupListeners()
    }

    private fun setupListeners() {
        btnConfirmar.setOnClickListener {
            val folder = selectedFolder
            if (folder != null) {
                // Launch coroutine to call suspend function
                CoroutineScope(Dispatchers.Main).launch {
                    val ok = onConfirm(folder)
                    if (ok) {
                        Toast.makeText(context, "Livro adicionado com sucesso.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Não foi possível adicionar o livro.", Toast.LENGTH_SHORT).show()
                    }
                    dismiss()
                }
            }
        }

        btnCancelar.setOnClickListener {
            dismiss()
        }
    }


    private fun onCancelarClick() {
        dismiss()
    }
}
