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

class ExemplarReservadoFragment(
    var exemplar: ExemplarData
) : Fragment() {

    private val livroAPI by lazy {
        RetrofitClient.create(context).create(LivroAPI::class.java)
    }

    private lateinit var exemplarReservadoCapa: ImageView
    private lateinit var exemplarReservadoNome: TextView
    private lateinit var exemplarReservadoCondicoes: TextView
    private lateinit var exemplarReservadoDataEmprestimo: TextView
    private lateinit var exemplarReservadoDataLimite: TextView
    private lateinit var exemplarReservadoVerBtn: Button
    private lateinit var exemplarReservadoEditarBtn: Button
    private lateinit var exemplarReservadoCancelarBtn: Button
    private lateinit var exemplarReservadoIndisponivelBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exemplar_reservado, container, false)

        exemplarReservadoCapa = view.findViewById(R.id.exemplar_reservado_capa)
        exemplarReservadoNome = view.findViewById(R.id.exemplar_reservado_nome)
        exemplarReservadoCondicoes = view.findViewById(R.id.exemplar_reservado_condicoes)
        exemplarReservadoDataLimite = view.findViewById(R.id.exemplar_reservado_data_limite)
        exemplarReservadoDataEmprestimo = view.findViewById(R.id.exemplar_reservado_data_reserva)
        exemplarReservadoVerBtn = view.findViewById(R.id.exemplar_reservado_ver_btn)
        exemplarReservadoEditarBtn = view.findViewById(R.id.exemplar_reservado_editar_btn)
        exemplarReservadoCancelarBtn = view.findViewById(R.id.exemplar_reservado_cancelar_btn)
        exemplarReservadoIndisponivelBtn = view.findViewById(R.id.exemplar_reservado_indisponivel_btn)

        exemplarReservadoVerBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, LivroFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        exemplarReservadoEditarBtn.setOnClickListener {
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

        exemplarReservadoCancelarBtn.setOnClickListener {
            // TODO: ação do botão "Cancelar Reserva"
        }

        exemplarReservadoIndisponivelBtn.setOnClickListener {
            // TODO: ação do botão "Tornar Indisponível"
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("Exemplar")
    }
}
