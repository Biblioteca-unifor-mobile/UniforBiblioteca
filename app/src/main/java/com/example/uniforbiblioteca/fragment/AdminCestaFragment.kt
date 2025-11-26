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
import com.example.uniforbiblioteca.dataclass.CartItem
import com.example.uniforbiblioteca.dataclass.LivroData
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


        // Lista de placeholders convertida para CartItem
        val livros = listOf(
            CartItem(book = LivroData(titulo = "Livro 1", autor = "Autor 1", imageUrl = "https://placehold.co/200x300/png")),
            CartItem(book = LivroData(titulo = "Livro 2", autor = "Autor 2", imageUrl = "https://placehold.co/200x300/png")),
            CartItem(book = LivroData(titulo = "Livro 3", autor = "Autor 3", imageUrl = "https://placehold.co/200x300/png")),
            CartItem(book = LivroData(titulo = "Livro 4", autor = "Autor 4", imageUrl = "https://placehold.co/200x300/png")),
            CartItem(book = LivroData(titulo = "Livro 5", autor = "Autor 5", imageUrl = "https://placehold.co/200x300/png"))
        )

        // Adapter
        val adapter = CestaAdapter(livros) { item ->
            // Aqui estamos tratando o clique (que no novo CestaAdapter é long click para delete)
            // Adaptando para navegação caso desejado, ou mantendo mock.
            val livro = item.book
            if (livro != null) {
                val fragment = LivroFragment.newInstance(livro)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.adminFragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
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
