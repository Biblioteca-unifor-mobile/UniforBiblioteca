package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dialog.AcervoFiltroDialogFragment
import com.example.uniforbiblioteca.rvadapter.AcervoAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AcervoFragment : Fragment() {


    lateinit var cestaFAB: FloatingActionButton
    lateinit var filtroBtn: Button

    lateinit var voltar: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_acervo, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.acervoRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        cestaFAB = view.findViewById(R.id.cestaFAB)
        voltar = view.findViewById(R.id.voltar_acervo)

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
        val adapter = AcervoAdapter(livros) { livro ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, LivroFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = adapter

        cestaFAB.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, CestaFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        filtroBtn = view.findViewById(R.id.acervoFilterBtn)

        filtroBtn.setOnClickListener {
            val dialog = AcervoFiltroDialogFragment()
            dialog.show(parentFragmentManager, "AcervoFiltroDialog")
        }

        voltar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("acervo")
    }
}