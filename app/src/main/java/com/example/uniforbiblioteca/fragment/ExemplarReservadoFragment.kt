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

class ExemplarReservadoFragment : Fragment() {

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

            val dialog = EstadoExemplarDialog()
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
