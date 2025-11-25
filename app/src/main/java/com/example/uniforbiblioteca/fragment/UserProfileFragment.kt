package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.uniforbiblioteca.R
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.rvadapter.AcervoAdapter
import com.example.uniforbiblioteca.rvadapter.HistoricoAdapter
import com.example.uniforbiblioteca.rvadapter.PastaAdapter
import com.example.uniforbiblioteca.ui.DialogConfirmarDeletarUser

class UserProfileFragment : androidx.fragment.app.Fragment() {

    // TextViews
    private lateinit var userName: TextView
    private lateinit var userMatricula: TextView
    private lateinit var userEmail: TextView
    private lateinit var lblEmprestimos: TextView
    private lateinit var lblReservas: TextView
    private lateinit var lblListas: TextView

    // Buttons
    private lateinit var cestaBtn: Button
    private lateinit var deleteBtn: Button


    // RecyclerViews
    private lateinit var rvEmprestimos: RecyclerView
    private lateinit var rvReservas: RecyclerView
    private lateinit var rvListas: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        userName = view.findViewById(R.id.user_profile_name)
        userMatricula = view.findViewById(R.id.user_profile_matricula)
        userEmail = view.findViewById(R.id.user_profile_email)
        lblEmprestimos = view.findViewById(R.id.lbl_user_profile_emprestimos)
        lblReservas = view.findViewById(R.id.lbl_user_profile_reservas)
        lblListas = view.findViewById(R.id.lbl_user_profile_listas)

        cestaBtn = view.findViewById(R.id.user_profile_cesta_button)
        deleteBtn = view.findViewById(R.id.user_profile_delete_button)

        rvEmprestimos = view.findViewById(R.id.user_profile_emprestimos_rv)
        rvReservas = view.findViewById(R.id.user_profile_reservas_rv)
        rvListas = view.findViewById(R.id._user_profile_listas_rv)

        rvEmprestimos.layoutManager = LinearLayoutManager(requireContext())
        rvReservas.layoutManager = LinearLayoutManager(requireContext())
        rvListas.layoutManager = LinearLayoutManager(requireContext())


        val emprestados = listOf(
            LivroCardData(
                "1",
                "Livro 1",
                "Autor 1",
                "5 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                "2",
                "Livro 2",
                "Autor 2",
                "8 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                "3",
                "Livro 3",
                "Autor 3",
                "8 dias atrás",
                "https://placehold.co/200x300/png"
            )
        )

        // Adapter
        val adapterEmprestimo = HistoricoAdapter(emprestados) { livro ->
        }

        rvEmprestimos.adapter = adapterEmprestimo

        val reservados = listOf(
            LivroCardData(
                "1",
                "Livro 1",
                "Autor 1",
                "5 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                "2",
                "Livro 2",
                "Autor 2",
                "8 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                "3",
                "Livro 3",
                "Autor 3",
                "8 dias atrás",
                "https://placehold.co/200x300/png"
            )
        )

        // Adapter
        val adapterReserva = AcervoAdapter(emptyList()) { livro ->

        }

        rvReservas.adapter = adapterReserva
        // Adapter
        val adapterListas =
            PastaAdapter(mutableListOf()) { pasta ->
            }

        rvListas.adapter = adapterListas

        cestaBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminCestaFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        deleteBtn.setOnClickListener {
            DialogConfirmarDeletarUser
                .newInstance(userName.text.toString())
                .show(parentFragmentManager, "confirmarDeletarUser")
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("User Profile")
    }
}
