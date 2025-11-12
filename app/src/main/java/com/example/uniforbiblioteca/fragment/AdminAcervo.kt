package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dialog.AcervoFiltroDialogFragment
import com.example.uniforbiblioteca.rvadapter.AdminAcervoAdapter
import com.example.uniforbiblioteca.ui.DialogConfirmarDeletarLivro
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.uniforbiblioteca.viewmodel.AcervoViewModel

class AdminAcervo : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminAcervoAdapter
    private lateinit var filtroBtn: Button
    private lateinit var newBtn: FloatingActionButton

    // Retrofit e ViewModel
    private val livroAPI by lazy {
        RetrofitClient.create(requireContext()).create(LivroAPI::class.java)
    }

    private val viewModel: AcervoViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AcervoViewModel(livroAPI) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_admin_acervo, container, false)

        recyclerView = view.findViewById(R.id.admin_acervo_rv)
        filtroBtn = view.findViewById(R.id.admin_acervo_filtro_button)
        newBtn = view.findViewById(R.id.admin_acervo_fab)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AdminAcervoAdapter(emptyList(), ::onItemClick, ::onDeleteClicked, ::onEditClicked)
        recyclerView.adapter = adapter

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

        carregarLivros()

        return view
    }

    private fun carregarLivros() {
        viewModel.carregarLivros(
            onLoaded = { livros ->
                Log.d("ADMIN_ACERVO", "Livros carregados: ${livros.size}")
                adapter.updateData(livros)
            },
            onError = { e ->
                Log.e("ADMIN_ACERVO", "Erro ao carregar livros: ${e.message}")
            }
        )
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("acervo")
    }

    private fun onItemClick(livro: LivroData) {
        val fragment = AdminLivroFragment.newInstance(livro)
        parentFragmentManager.beginTransaction()
            .replace(R.id.adminFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun onEditClicked(livro: LivroData) {
        val fragment = AdminEditLivroFragment.newInstance(livro)
        parentFragmentManager.beginTransaction()
            .replace(R.id.adminFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun onDeleteClicked(livro: LivroData) {
        DialogConfirmarDeletarLivro
            .newInstance(livro)
            .show(parentFragmentManager, "confirmarDeletarLivro")
    }
}
