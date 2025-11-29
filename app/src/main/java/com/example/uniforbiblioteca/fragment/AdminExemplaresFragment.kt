package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.dialog.DialogAddExemplares
import com.example.uniforbiblioteca.dataclass.Exemplar
import com.example.uniforbiblioteca.dataclass.ExemplarData
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.rvadapter.ExemplarAdapter
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class AdminExemplaresFragment : Fragment() {

    lateinit var newFAB: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExemplarAdapter

    private var livroData: LivroData? = null

    // Retrofit API
    private val livroAPI by lazy {
        RetrofitClient.create(requireContext()).create(LivroAPI::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        livroData = arguments?.getSerializable("livro") as? LivroData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_exemplares, container, false)

        newFAB = view.findViewById(R.id.admin_exemplares_fab)
        recyclerView = view.findViewById(R.id.admin_exemplares_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inicializa adapter vazio
        adapter = ExemplarAdapter(emptyList()) { exemplar ->
            navegarParaDetalhesExemplar(exemplar)
        }
        recyclerView.adapter = adapter

        newFAB.setOnClickListener {
            // Abre dialog para adicionar exemplar
            // Se tivermos o ID do livro, podemos passar para o Dialog
            val dialog = DialogAddExemplares()
            livroData?.id?.let { bookId ->
                val args = Bundle()
                args.putString("bookId", bookId)
                dialog.arguments = args
            }
            dialog.show(parentFragmentManager, "AddExemplares")
        }

        carregarExemplares()

        return view
    }

    private fun carregarExemplares() {

        val bookId = livroData?.id

        lifecycleScope.launch {
            try {
                val exemplaresData: List<ExemplarData> = if (bookId != null) {
                     livroAPI.getBookCopiesByBook(bookId)
                } else {

                    livroAPI.getBookCopies()
                }


                val exemplaresUI = exemplaresData.map { data ->
                    Exemplar(
                        id = 0, // O adapter usa Int, mas a API retorna String. Ajuste necessário no Adapter futuramente. Por enquanto 0 ou hashCode.
                        livroId = 0,
                        num = data.copyNumber ?: 0,
                        status = data.status ?: "Indisponível",
                        capa = "https://placehold.co/200x300/png" // URL placeholder ou do livroData?.imageUrl
                    )
                }
                
                adapter.updateData(exemplaresUI)

            } catch (e: Exception) {
                Log.e("ADMIN_EXEMPLARES", "Erro ao carregar exemplares: ${e.message}")
                Toast.makeText(context, "Erro ao carregar exemplares", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navegarParaDetalhesExemplar(exemplar: Exemplar) {
        val fragment = when (exemplar.status) {
            "Disponível", "DISPONIVEL" -> ExemplarDisponivelFragment::class.java
            "Indisponivel", "INDISPONIVEL" -> ExemplarIndisponivelFragment::class.java
            "Reservado", "RESERVADO" -> ExemplarReservadoFragment::class.java
            "Emprestado", "ALUGADO" -> ExemplarAlugadoFragment::class.java
            else -> null
        }

        fragment?.let {
             parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, it, null)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("exemplares")
        // Recarrega ao voltar, caso tenha adicionado novo exemplar
        carregarExemplares()
    }

    companion object {
        fun newInstance(livro: LivroData?): AdminExemplaresFragment {
            val fragment = AdminExemplaresFragment()
            val args = Bundle()
            args.putSerializable("livro", livro)
            fragment.arguments = args
            return fragment
        }
    }
}
