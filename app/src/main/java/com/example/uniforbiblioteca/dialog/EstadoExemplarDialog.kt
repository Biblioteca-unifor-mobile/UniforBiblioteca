package com.example.uniforbiblioteca.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.uniforbiblioteca.R

class EstadoExemplarDialog : DialogFragment() {

    private lateinit var muitoBoaCondicao: TextView
    private lateinit var boaCondicao: TextView
    private lateinit var conservadoCondicao: TextView
    private lateinit var ruimCondicao: TextView
    private lateinit var muitoRuimCondicao: TextView
    private lateinit var confirmarPedido: Button
    private lateinit var cancelarPedido: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = Dialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_estado_exemplar, null)
        builder.setContentView(view)

        // Inicialização das views
        muitoBoaCondicao = view.findViewById(R.id.muito_boa_condicao)
        boaCondicao = view.findViewById(R.id.boa_condicao)
        conservadoCondicao = view.findViewById(R.id.conservado_condicao)
        ruimCondicao = view.findViewById(R.id.ruim_condicao)
        muitoRuimCondicao = view.findViewById(R.id.muito_ruim_condicao)
        confirmarPedido = view.findViewById(R.id.confirmar_pedido)
        cancelarPedido = view.findViewById(R.id.cancelar_pedido)

        // Listeners de clique (vazios por enquanto)
        muitoBoaCondicao.setOnClickListener { }
        boaCondicao.setOnClickListener { }
        conservadoCondicao.setOnClickListener { }
        ruimCondicao.setOnClickListener { }
        muitoRuimCondicao.setOnClickListener { }

        confirmarPedido.setOnClickListener { }
        cancelarPedido.setOnClickListener { }

        return builder
    }
}