package com.example.uniforbiblioteca.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.example.uniforbiblioteca.R

class AcervoFiltroDialogFragment : DialogFragment() {

    lateinit var tituloFiltro: EditText
    lateinit var autoriaFiltro: EditText
    lateinit var assuntoFiltro: EditText
    lateinit var serieFiltro: EditText
    lateinit var isbnFiltro: EditText
    lateinit var anoFiltro: EditText
    lateinit var pesquisarBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_acervo_filtro, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tituloFiltro = view.findViewById(R.id.tituloFiltroAcervo)
        autoriaFiltro = view.findViewById(R.id.autoriaFiltroAcervo)
        assuntoFiltro = view.findViewById(R.id.assuntoFiltroAcervo)
        serieFiltro = view.findViewById(R.id.serieFiltroAcervo)
        isbnFiltro = view.findViewById(R.id.isbnFiltroAcervo)
        anoFiltro = view.findViewById(R.id.anoFiltroAcervo)
        pesquisarBtn = view.findViewById(R.id.acervoPesquisarButton)

        pesquisarBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }



}