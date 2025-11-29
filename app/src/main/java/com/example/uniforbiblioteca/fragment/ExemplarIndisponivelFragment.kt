package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.ExemplarData
import com.example.uniforbiblioteca.dialog.EstadoExemplarDialog

class ExemplarIndisponivelFragment(
    var exemplar: ExemplarData
) : Fragment() {

    private val livroAPI by lazy {
        RetrofitClient.create(context).create(LivroAPI::class.java)
    }

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
            val dialog = EstadoExemplarDialog{ condicao ->
                try {
                    val novo = ExemplarData(status = exemplar.status, condition = condicao)
                    livroAPI.patchBookCopy(exemplar.id!!, novo)
                    exemplar.condition = novo.condition
                    Toast.makeText(requireContext(), "Exemplar alterado com sucesso", Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Toast.makeText(requireContext(), "Não foi possivel alterar o exemplar", Toast.LENGTH_SHORT).show()
                }
            }
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
