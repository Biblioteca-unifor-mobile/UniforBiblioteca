package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.rvadapter.CestaAdapter
import com.example.uniforbiblioteca.ui.DialogWarningConfirmar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CestaFragment : Fragment() {
    lateinit var voltar: Button

    lateinit var addFAB: FloatingActionButton

    lateinit var confirmarBtn: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cesta, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.cestaRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

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
        )

        // Adapter
        val adapter = CestaAdapter(livros) { livro ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, LivroFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = adapter

        voltar = view.findViewById(R.id.voltar_cesta)

        voltar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        addFAB = view.findViewById(R.id.cesta_add_fab)

        addFAB.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, AcervoFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        confirmarBtn = view.findViewById(R.id.continuar_cesta)

        confirmarBtn.setOnClickListener {
            val dialog = DialogWarningConfirmar()
            dialog.show(parentFragmentManager, "Warn")
        }



        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("cesta")
    }
}