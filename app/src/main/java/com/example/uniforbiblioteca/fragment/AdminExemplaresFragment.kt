package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.dialog.DialogAddExemplares
import com.example.uniforbiblioteca.dataclass.Exemplar
import com.example.uniforbiblioteca.rvadapter.ExemplarAdapter
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AdminExemplaresFragment : Fragment() {

    lateinit var newFAB: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_exemplares, container, false)

        newFAB = view.findViewById(R.id.admin_exemplares_fab)

        val recyclerView: RecyclerView = view.findViewById(R.id.admin_exemplares_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Lista de placeholders
        val exemplares = listOf(
            Exemplar(
                id = 1,
                livroId = 101,
                num = 1,
                status = "Disponível",
                capa = "https://placehold.co/200x300/png"
            ),
            Exemplar(
                id = 2,
                livroId = 101,
                num = 2,
                status = "Emprestado",
                capa = "https://placehold.co/200x300/png"
            ),
            Exemplar(
                id = 3,
                livroId = 101,
                num = 3,
                status = "Reservado",
                capa = "https://placehold.co/200x300/png"
            ),
            Exemplar(
                id = 4,
                livroId = 101,
                num = 4,
                status = "Indisponivel",
                capa = "https://placehold.co/200x300/png"
            )
        )


        // Adapter
        val adapter = ExemplarAdapter(exemplares) { exemplar ->
            if (exemplar.status == "Disponível") {
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.adminFragmentContainer,
                        ExemplarDisponivelFragment::class.java,
                        null
                    )
                    .addToBackStack(null)
                    .commit()
            }

            if (exemplar.status == "Indisponivel") {
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.adminFragmentContainer,
                        ExemplarIndisponivelFragment::class.java,
                        null
                    )
                    .addToBackStack(null)
                    .commit()
            }

            if (exemplar.status == "Reservado") {
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.adminFragmentContainer,
                        ExemplarReservadoFragment::class.java,
                        null
                    )
                    .addToBackStack(null)
                    .commit()
            }

            if (exemplar.status == "Emprestado") {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.adminFragmentContainer, ExemplarAlugadoFragment::class.java, null)
                    .addToBackStack(null)
                    .commit()
            }
        }

        newFAB.setOnClickListener {
            val dialog = DialogAddExemplares()
            dialog.show(parentFragmentManager, "AddExemplares")
        }

        recyclerView.adapter = adapter

        return view
    }



    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("exemplares")
    }

}