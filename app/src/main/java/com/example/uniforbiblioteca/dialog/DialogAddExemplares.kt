package com.example.uniforbiblioteca.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.uniforbiblioteca.R

class DialogAddExemplares : DialogFragment() {

    private lateinit var tituloTextView: TextView
    private lateinit var menosExemplarButton: ImageButton
    private lateinit var maisExemplarButton: ImageButton
    private lateinit var quantidadeEditText: EditText
    private lateinit var confirmarButton: Button
    private lateinit var cancelarButton: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.dialog_add_exemplares, null)

        // Inicializa views
        tituloTextView = view.findViewById(R.id.textView59)
        menosExemplarButton = view.findViewById(R.id.menos_exemplar)
        maisExemplarButton = view.findViewById(R.id.mais_exemplar)
        quantidadeEditText = view.findViewById(R.id.quantidade_adicionar)
        confirmarButton = view.findViewById(R.id.confirmar_add_exemplares)
        cancelarButton = view.findViewById(R.id.cancelar_add_exemplares)

        // Listeners vazios
        menosExemplarButton.setOnClickListener {
            // TODO: diminuir quantidade
        }

        maisExemplarButton.setOnClickListener {
            // TODO: aumentar quantidade
        }

        confirmarButton.setOnClickListener {
            // TODO: confirmar adição
        }

        cancelarButton.setOnClickListener {
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}