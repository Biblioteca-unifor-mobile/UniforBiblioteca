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

class ExemplarDisponivelFragment : Fragment() {

    private lateinit var exemplarDisponivelCapa: ImageView
    private lateinit var exemplarDisponivelCondicoes: TextView
    private lateinit var exemplarDisponivelUltimoAluguel: TextView
    private lateinit var exemplarDisponivelVerBtn: Button
    private lateinit var exemplarDisponivelEditarBtn: Button
    private lateinit var exemplarDisponivelCancelarBtn: Button
    private lateinit var exemplarDisponivelIndisponivelBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exemplar_disponivel, container, false)

        exemplarDisponivelCapa = view.findViewById(R.id.exemplar_disponivel_capa)
        exemplarDisponivelCondicoes = view.findViewById(R.id.exemplar_disponivel_condicoes)
        exemplarDisponivelUltimoAluguel = view.findViewById(R.id.exemplar_disponivel_ultimo_emprestimo)
        exemplarDisponivelVerBtn = view.findViewById(R.id.exemplar_disponivel_ver_btn)
        exemplarDisponivelEditarBtn = view.findViewById(R.id.exemplar_disponivel_editar_btn)
        exemplarDisponivelCancelarBtn = view.findViewById(R.id.exemplar_disponivel_cesta_btn)
        exemplarDisponivelIndisponivelBtn = view.findViewById(R.id.exemplar_disponivel_indisponivel_btn)

        exemplarDisponivelVerBtn.setOnClickListener {
            // TODO: ação do botão "Ver Livro"

            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, LivroFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        exemplarDisponivelEditarBtn.setOnClickListener {
                val dialog = EstadoExemplarDialog()
                dialog.show(parentFragmentManager, "EstadoExemplar")
        }

        exemplarDisponivelCancelarBtn.setOnClickListener {
            // TODO: ação do botão "Adicionar à Cesta"
        }

        exemplarDisponivelIndisponivelBtn.setOnClickListener {
            // TODO: ação do botão "Tornar Indisponível"
        }

        return view
    }
    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("Exemplar")
    }
}
