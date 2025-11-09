package com.example.uniforbiblioteca.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.uniforbiblioteca.R

class DialogConfirmarPedido : DialogFragment() { // 1. Removida a reserva do construtor

    private lateinit var messageTextView: TextView
    private lateinit var confirmarButton: Button
    private lateinit var cancelarButton: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val reserva = arguments?.getBoolean(ARG_RESERVA, false) ?: false

        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.dialog_confirmar_pedido, null)

        messageTextView = view.findViewById(R.id.message)
        confirmarButton = view.findViewById(R.id.confirmar_pedido)
        cancelarButton = view.findViewById(R.id.cancelar_pedido)

        if (reserva) {
            messageTextView.text = "Você terá até dia 17/09/2025 às 10:25 para buscar seus livros! Avisaremos quando suas reservas estiverem disponíveis"
        } else {
            messageTextView.text = "Você terá até dia 17/09/2025 às 10:25 para buscar seus livros!"
        }

        confirmarButton.setOnClickListener {
            dismiss()
        }

        cancelarButton.setOnClickListener {
            dismiss()
        }

        builder.setView(view)




        return builder.create()
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