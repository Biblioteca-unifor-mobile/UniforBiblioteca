package com.example.uniforbiblioteca.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.uniforbiblioteca.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstadoExemplarDialog(
    val onConfirm: suspend (newState: String) -> Unit
) : DialogFragment() {

    private lateinit var muitoBoaCondicao: TextView
    private lateinit var boaCondicao: TextView
    private lateinit var conservadoCondicao: TextView
    private lateinit var ruimCondicao: TextView
    private lateinit var muitoRuimCondicao: TextView
    private lateinit var confirmarPedido: Button
    private lateinit var cancelarPedido: Button
    var selected = ""

    private val scope = CoroutineScope(Dispatchers.Main)


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = Dialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_estado_exemplar, null)
        builder.setContentView(view)

        muitoBoaCondicao = view.findViewById(R.id.muito_boa_condicao)
        boaCondicao = view.findViewById(R.id.boa_condicao)
        conservadoCondicao = view.findViewById(R.id.conservado_condicao)
        ruimCondicao = view.findViewById(R.id.ruim_condicao)
        muitoRuimCondicao = view.findViewById(R.id.muito_ruim_condicao)
        confirmarPedido = view.findViewById(R.id.confirmar_pedido)
        cancelarPedido = view.findViewById(R.id.cancelar_pedido)

        val listaEstados = listOf(
            muitoBoaCondicao,
            boaCondicao,
            conservadoCondicao,
            ruimCondicao,
            muitoRuimCondicao
        )

        fun selecionarEstado(selecionado: TextView) {
            listaEstados.forEach { estado ->
                estado.setTextColor(requireContext().getColor(R.color.black))

                // Remove o ✓ se existir
                estado.text = estado.text.toString().replace("✓ ", "")
            }

            // Marca o selecionado
            selecionado.setTextColor(requireContext().getColor(R.color.light_blue_A400))
            selected = selecionado.text.toString()
            selecionado.text = "✓ ${selecionado.text}"
        }

        muitoBoaCondicao.setOnClickListener { selecionarEstado(muitoBoaCondicao) }
        boaCondicao.setOnClickListener { selecionarEstado(boaCondicao) }
        conservadoCondicao.setOnClickListener { selecionarEstado(conservadoCondicao) }
        ruimCondicao.setOnClickListener { selecionarEstado(ruimCondicao) }
        muitoRuimCondicao.setOnClickListener { selecionarEstado(muitoRuimCondicao) }

        cancelarPedido.setOnClickListener {
            scope.launch {
                onConfirm(selected)
                dismiss()
            }
        }

        confirmarPedido.setOnClickListener {

            dismiss()
        }

        return builder
    }

}