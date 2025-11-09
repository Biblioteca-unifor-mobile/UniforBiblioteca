package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.rvadapter.EmprestadoAdapter


class HomeFragment : Fragment() {


    lateinit var nome: TextView

    lateinit var matricula: TextView
    lateinit var sairBtn: Button

    lateinit var recente1: ImageView
    lateinit var recente2: ImageView
    lateinit var recente3: ImageView
    lateinit var recente4: ImageView
    lateinit var recente5: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewHome)

        recente1 = view.findViewById(R.id.recente_1)
        recente2 = view.findViewById(R.id.recente_2)
        recente3 = view.findViewById(R.id.recente_3)
        recente4 = view.findViewById(R.id.recente_4)
        recente5 = view.findViewById(R.id.recente_5)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val livros = listOf(
            LivroCardData(
                1,
                "Livro 1",
                "Autor 1",
                "Finaliza: 13 de outubro de 2025",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                2,
                "Livro 2",
                "Autor 2",
                "Finaliza: 13 de outubro de 2025",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                3,
                "Livro 3",
                "Autor 3",
                "Finaliza: 22 de outubro de 2025",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                4,
                "Livro 4",
                "Autor 4",
                "Finaliza: 22 de outubro de 2025",
                "https://placehold.co/200x300/png"
            ),
        )

        //
        val adapter = EmprestadoAdapter(livros) { livro ->
            verLivro()
        }

        recyclerView.adapter = adapter

        sairBtn = view.findViewById(R.id.sairBtn)

        sairBtn.setOnClickListener {
            (activity as? MainActivity)?.sair()
        }

        recente1.setOnClickListener { verLivro() }
        recente2.setOnClickListener { verLivro() }
        recente3.setOnClickListener { verLivro() }
        recente4.setOnClickListener { verLivro() }
        recente5.setOnClickListener { verLivro() }

        matricula = view.findViewById(R.id.home_matricula)
        nome = view.findViewById(R.id.home_nome)

        matricula.text = "2412819"
        nome.text = "Olá, ${"João"}"


        return view
    }

    fun verLivro(){
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, LivroFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }


    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("home")
    }
}