package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.dialog.EstadoExemplarDialog

class ExemplarIndisponivelFragment : Fragment() {

    private lateinit var exemplarIndisponivelCapa: ImageView
    private lateinit var exemplarIndisponivelCondicoes: TextView
    private lateinit var exemplarIndisponivelDataIndisponivel: TextView
    private lateinit var exemplarIndisponivelVerBtn: Button
    private lateinit var exemplarIndisponivelEditarBtn: Button
    private lateinit var exemplarIndisponivelInindisponivelBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exemplar_indisponivel, container, false)

        exemplarIndisponivelCapa = view.findViewById(R.id.exemplar_indisponivel_capa)
        exemplarIndisponivelCondicoes = view.findViewById(R.id.exemplar_indisponivel_condicoes)
        exemplarIndisponivelDataIndisponivel = view.findViewById(R.id.exemplar_indisponivel_data_indisponivel)
        exemplarIndisponivelVerBtn = view.findViewById(R.id.exemplar_indisponivel_ver_btn)
        exemplarIndisponivelEditarBtn = view.findViewById(R.id.exemplar_indisponivel_editar_btn)
        exemplarIndisponivelInindisponivelBtn = view.findViewById(R.id.exemplar_indisponivel_inindisponivel_btn)

        exemplarIndisponivelVerBtn.setOnClickListener {
            // TODO: ação do botão "Ver Livro"

            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, LivroFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        exemplarIndisponivelEditarBtn.setOnClickListener {

            val dialog = EstadoExemplarDialog()
            dialog.show(parentFragmentManager, "EstadoExemplar")
        }

        exemplarIndisponivelInindisponivelBtn.setOnClickListener {
            // TODO: ação do botão "Tornar Disponível"
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("Exemplar")
    }
}
