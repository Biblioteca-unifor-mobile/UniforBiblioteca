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
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dialog.AcervoFiltroDialogFragment
import com.example.uniforbiblioteca.rvadapter.AcervoAdapter
import com.example.uniforbiblioteca.viewmodel.AcervoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AcervoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AcervoAdapter
    private lateinit var filtroBtn: Button
    private lateinit var cestaFAB: FloatingActionButton
    private lateinit var pesquisa: EditText
    private lateinit var voltarBtn: Button

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_acervo, container, false)

        recyclerView = view.findViewById(R.id.acervoRecyclerView)
        filtroBtn = view.findViewById(R.id.acervoFilterBtn)
        cestaFAB = view.findViewById(R.id.cestaFAB)
        voltarBtn = view.findViewById(R.id.voltar_acervo)
        pesquisa = view.findViewById(R.id.pesquisa)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AcervoAdapter(mutableListOf()) { livro ->
            onLivroClicked(livro)
        }
        recyclerView.adapter = adapter

        filtroBtn.setOnClickListener {
            val dialog = AcervoFiltroDialogFragment()
            dialog.show(parentFragmentManager, "AcervoFiltroDialog")
        }

        cestaFAB.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, CestaFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        voltarBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        carregarLivros()

        pesquisa.doAfterTextChanged { text ->
            pesquisarLivro(text.toString())
        }

        return view
    }

    private fun carregarLivros() {
        viewModel.carregarLivros(
            onLoaded = { livros ->
                Log.d("ACERVO_FRAGMENT", "Livros carregados: ${livros.size}")
                display = livros as MutableList<LivroData>
                listaLivros = display
                adapter.updateData(display)
            },
            onError = { e ->
                Log.e("ACERVO_FRAGMENT", "Erro ao carregar livros: ${e.message}")
            }
        )
    }

    private fun pesquisarLivro(partial: String){
        display = mutableListOf()
        for (livro in listaLivros){
            val title: String = livro.titulo.toString()
            if (title.lowercase().startsWith(partial.lowercase())){
                display.add(livro)
            }
        }
        adapter.updateData(display)
    }

    private fun onLivroClicked(livro: LivroData) {
        val fragment = LivroFragment.newInstance(livro)
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("acervo")
    }
}
