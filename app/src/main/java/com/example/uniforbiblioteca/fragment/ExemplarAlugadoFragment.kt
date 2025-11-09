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

class ExemplarAlugadoFragment : Fragment() {

    private lateinit var exemplarAlugadoCapa: ImageView
    private lateinit var exemplarAlugadoNome: TextView
    private lateinit var exemplarAlugadoCondicoes: TextView
    private lateinit var exemplarAlugadoDataEmprestimo: TextView
    private lateinit var exemplarAlugadoDataLimite: TextView
    private lateinit var exemplarAlugadoRenovacoes: TextView
    private lateinit var exemplarAlugadoDivida: TextView
    private lateinit var exemplarAlugadoVerBtn: Button
    private lateinit var exemplarAlugadoEditarBtn: Button
    private lateinit var exemplarAlugadoRenovarBtn: Button
    private lateinit var exemplarAlugadoDevolverBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exemplar_alugado, container, false)

        exemplarAlugadoCapa = view.findViewById(R.id.exemplar_alugado_capa)
        exemplarAlugadoNome = view.findViewById(R.id.exemplar_alugado_nome)
        exemplarAlugadoCondicoes = view.findViewById(R.id.exemplar_alugado_condicoes)
        exemplarAlugadoDataEmprestimo = view.findViewById(R.id.exemplar_alugado_data_emprestimo)
        exemplarAlugadoDataLimite = view.findViewById(R.id.exemplar_alugado_data_limite)
        exemplarAlugadoRenovacoes = view.findViewById(R.id.exemplar_alugado_renovacoes)
        exemplarAlugadoDivida = view.findViewById(R.id.exemplar_alugado_divida)
        exemplarAlugadoVerBtn = view.findViewById(R.id.exemplar_alugado_ver_btn)
        exemplarAlugadoEditarBtn = view.findViewById(R.id.exemplar_alugado_editar_btn)
        exemplarAlugadoRenovarBtn = view.findViewById(R.id.exemplar_alugado_renovar_btn)
        exemplarAlugadoDevolverBtn = view.findViewById(R.id.exemplar_alugado_devolver_btn)

        exemplarAlugadoVerBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, LivroFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        exemplarAlugadoEditarBtn.setOnClickListener {
            val dialog = EstadoExemplarDialog()
            dialog.show(parentFragmentManager, "EstadoExemplar")
        }

        exemplarAlugadoRenovarBtn.setOnClickListener {

        }

        exemplarAlugadoDevolverBtn.setOnClickListener {

        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("Exemplar")
    }
}
