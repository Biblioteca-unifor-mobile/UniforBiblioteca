package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.api.CartAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dialog.HistoricoFilterDialogFragment
import com.example.uniforbiblioteca.rvadapter.HistoricoAdapter
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoricoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoricoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val cartAPI by lazy {
        RetrofitClient.create(context).create(CartAPI::class.java)
    }

    private lateinit var adapter: HistoricoAdapter

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_historico, container, false)

        // RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewHistorico) // Ajuste o id para o seu XML
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Adapter
        adapter = HistoricoAdapter(mutableListOf()) { loan ->
            // Aqui extraímos o LivroData do Loan para passar para o LivroFragment
            // Loan -> bookCopy -> book (LoanBook)
            val loanBook = loan.bookCopy?.book
            if (loanBook != null) {
                // Converter LoanBook para LivroData
                val livro = LivroData(
                    id = loanBook.id,
                    titulo = loanBook.titulo,
                    autor = loanBook.autor,
                    imageUrl = loanBook.imageUrl
                    // Outros campos ficam nulos/padrão
                )
                
                // Navegar para detalhes
                val fragment = LivroFragment.newInstance(livro)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.mainFragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                Log.e("HISTORICO", "Livro nulo no histórico")
            }
        }

        recyclerView.adapter = adapter
        
        carregarHistorico()

        return view
    }

    private fun carregarHistorico() {
        lifecycleScope.launch {
            try {
                // getMyLoans retorna lista de empréstimos (ativos e passados)
                val loans = cartAPI.getMyLoans()
                adapter.updateItems(loans.toMutableList())
            } catch (e: Exception) {
                Log.e("HISTORICO", "Erro ao carregar histórico: ${e.message}")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnFiltro = view.findViewById<Button>(R.id.historicoFiltroBtn)

        btnFiltro.setOnClickListener {
            val dialog = HistoricoFilterDialogFragment()
            dialog.show(parentFragmentManager, "HistoricoFiltroDialog")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoricoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoricoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("historico")
    }
}
