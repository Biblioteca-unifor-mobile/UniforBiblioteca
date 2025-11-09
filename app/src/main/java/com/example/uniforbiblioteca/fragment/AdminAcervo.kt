package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.rvadapter.AdminAcervoAdapter
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dialog.AcervoFiltroDialogFragment
import com.example.uniforbiblioteca.ui.DialogConfirmarDeletarLivro
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdminAcervo : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var filtroBtn: Button
    lateinit var newBtn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_acervo, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.admin_acervo_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        newBtn = view.findViewById(R.id.admin_acervo_fab)

        // Lista de placeholders
        val livros = listOf(
            LivroCardData(
                1,
                "Livro 1",
                "Autor 1",
                "5 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                2,
                "Livro 2",
                "Autor 2",
                "8 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                3,
                "Livro 3",
                "Autor 3",
                "8 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                4,
                "Livro 4",
                "Autor 4",
                "30 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                5,
                "Livro 5",
                "Autor 5",
                "50 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                6,
                "Livro 6",
                "Autor 6",
                "50 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                7,
                "Livro 7",
                "Autor 7",
                "70 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                8,
                "Livro 8",
                "Autor 8",
                "90 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                9,
                "Livro 9",
                "Autor 9",
                "90 dias atrás",
                "https://placehold.co/200x300/png"
            )
        )

        // Adapter
        val adapter = AdminAcervoAdapter(
            livros,
            this::onItemClick,
            this::onDeleteClicked,
            this::onEditClicked
        )

        recyclerView.adapter = adapter


        filtroBtn = view.findViewById(R.id.admin_acervo_filtro_button)

        filtroBtn.setOnClickListener {
            val dialog = AcervoFiltroDialogFragment()
            dialog.show(parentFragmentManager, "AcervoFiltroDialog")
        }

        newBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminNovoLivroFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }


        return view
    }

    fun onItemClick(livro: LivroCardData){
        parentFragmentManager.beginTransaction()
            .replace(R.id.adminFragmentContainer, AdminLivroFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }

    fun onEditClicked(livro: LivroCardData) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.adminFragmentContainer, AdminEditLivroFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }


    fun onDeleteClicked(livro: LivroCardData) {
        DialogConfirmarDeletarLivro
            .newInstance(livro.titulo)
            .show(parentFragmentManager, "confirmarDeletarLivro")
    }



    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("acervo")
    }
}