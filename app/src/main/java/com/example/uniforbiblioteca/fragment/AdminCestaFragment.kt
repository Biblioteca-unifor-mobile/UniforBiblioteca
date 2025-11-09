package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.rvadapter.CestaAdapter
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.ui.DialogWarningConfirmar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminCestaFragment : Fragment() {

    // Views
    private lateinit var tituloText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var continuarBtn: Button
    private lateinit var addFAB: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_cesta, container, false)

        // Inicialização das views
        tituloText = view.findViewById(R.id.textView30)
        recyclerView = view.findViewById(R.id.admin_cesta_rv)
        continuarBtn = view.findViewById(R.id.admin_cesta_continuar)

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
                .replace(R.id.adminFragmentContainer, LivroFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = adapter

        addFAB = view.findViewById(R.id.admin_cesta_add_fab)

        addFAB.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminAcervo::class.java, null)
                .addToBackStack(null)
                .commit()
        }


        continuarBtn = view.findViewById(R.id.admin_cesta_continuar)

        continuarBtn.setOnClickListener {
            val dialog = DialogWarningConfirmar()
            dialog.show(parentFragmentManager, "Warn")
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("Cesta")
    }
}
