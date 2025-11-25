package com.example.uniforbiblioteca.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.uniforbiblioteca.R

class DialogCriarPasta (
    private val onCreateFolder: (String) -> Unit
): DialogFragment() {

    private lateinit var nomePastaTextView: TextView
    private lateinit var nomePastaEditText: EditText
    private lateinit var confirmarButton: Button
    private lateinit var cancelarButton: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.dialog_criar_pasta, null)

        // Inicializa views
        nomePastaTextView = view.findViewById(R.id.nome_criar_pasta)
        nomePastaEditText = view.findViewById(R.id.input_criar_pasta)
        confirmarButton = view.findViewById(R.id.confirmar_criar_pasta)
        cancelarButton = view.findViewById(R.id.cancelar_criar_pasta)

        // Listeners vazios
        confirmarButton.setOnClickListener {
            val novoNome = nomePastaEditText.text.toString().trim()

            if (novoNome.isNotEmpty()) {
                onCreateFolder(novoNome)
                dismiss()
            }
        }

        cancelarButton.setOnClickListener {
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}
