package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
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
    private lateinit var pesquisa: EditText
    private var listaLivros: MutableList<LivroData> = mutableListOf()
    private var display: MutableList<LivroData> = mutableListOf()

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
        pesquisa = view.findViewById(R.id.pesquisa)

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

        // Removido carregarLivros() daqui para garantir que seja chamado no onResume
        // carregarLivros()

        pesquisa.doAfterTextChanged { text ->
            pesquisarLivro(text.toString())
        }
        return view
    }

    private fun carregarLivros(forceReload: Boolean = false) {
        // Se forceReload for true, pode precisar adicionar lógica no ViewModel para limpar isLoaded
        if (forceReload) {
            // Assumindo que podemos resetar ou criar um método reload no ViewModel,
            // mas por enquanto vamos confiar que o ViewModel mantém o estado ou buscar novamente se necessário.
            // O problema descrito é que "não carrega de volta". Se o ViewModel mantiver o estado 'isLoaded = true',
            // ele retorna a lista antiga imediatamente. Se a lista antiga estiver vazia ou se quisermos atualizar,
            // precisamos forçar.
            // Para resolver o problema de navegação "não carrega", o ViewModel deve entregar os dados que já tem.
            // Se ele não entrega, é porque o LiveData/State não está sendo observado ou a lógica 'if (isLoaded)' falha?
            // O método carregarLivros usa um callback. Se isLoaded for true, ele chama onLoaded imediatamente.
            // Isso deve funcionar.
        }
        
        viewModel.carregarLivros(
            onLoaded = { livros ->
                Log.d("ADMIN_ACERVO", "Livros carregados: ${livros.size}")
                listaLivros = livros.toMutableList() // Atualiza a lista local para pesquisa
                if (pesquisa.text.toString().isEmpty()) {
                    adapter.updateData(livros)
                } else {
                    pesquisarLivro(pesquisa.text.toString())
                }
            },
            onError = { e ->
                Log.e("ADMIN_ACERVO", "Erro ao carregar livros: ${e.message}")
            }
        )
    }

    private fun pesquisarLivro(partial: String){
        display = mutableListOf()
        for (livro in listaLivros){
            val title: String = livro.titulo.toString()
            if (title.lowercase().contains(partial.lowercase())){ // Alterado de startsWith para contains para melhor UX
                display.add(livro)
            }
        }
        adapter.updateData(display)
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("acervo")
        // Chama carregarLivros no onResume para garantir atualização ao voltar da pilha
        carregarLivros() 
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
