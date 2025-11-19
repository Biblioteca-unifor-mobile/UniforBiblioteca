package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.api.CartAPI
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.rvadapter.EmprestadoAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class HomeFragment : Fragment() {


    lateinit var nome: TextView

    lateinit var matricula: TextView
    lateinit var sairBtn: Button

    lateinit var recente1: ImageView
    lateinit var recente2: ImageView
    lateinit var recente3: ImageView
    lateinit var recente4: ImageView
    lateinit var recente5: ImageView

    lateinit var txtNenhumEmprestimo: TextView

    private val livroAPI by lazy {
        RetrofitClient.create(context).create(LivroAPI::class.java)
    }
    
    private val cartAPI by lazy {
        RetrofitClient.create(context).create(CartAPI::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewHome)
        txtNenhumEmprestimo = view.findViewById(R.id.txt_nenhum_livro_emprestado)

        recente1 = view.findViewById(R.id.recente_1)
        recente2 = view.findViewById(R.id.recente_2)
        recente3 = view.findViewById(R.id.recente_3)
        recente4 = view.findViewById(R.id.recente_4)
        recente5 = view.findViewById(R.id.recente_5)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = EmprestadoAdapter(emptyList()) { livroCard ->
             // Pode implementar clique para detalhes do empréstimo se quiser
        }
        recyclerView.adapter = adapter

        sairBtn = view.findViewById(R.id.sairBtn)

        sairBtn.setOnClickListener {
            (activity as? MainActivity)?.sair()
        }

        matricula = view.findViewById(R.id.home_matricula)
        nome = view.findViewById(R.id.home_nome)

        matricula.text = "2412819"
        nome.text = "Olá, João"

        carregarLivrosRecentes()
        carregarEmprestimos(adapter, recyclerView)

        return view
    }

    private fun carregarEmprestimos(adapter: EmprestadoAdapter, recyclerView: RecyclerView) {
        lifecycleScope.launch {
            try {
                val response = cartAPI.getMyLoans()
                val loans = response.loans ?: emptyList()
                
                if (loans.isEmpty()) {
                    txtNenhumEmprestimo.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    txtNenhumEmprestimo.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    
                    // Converter Loan para LivroCardData para usar o Adapter existente
                    val emprestados = loans.map { loan ->
                         val book = loan.bookCopy?.book
                         val dataLimite = formatarData(loan.dataLimite)
                         LivroCardData(
                            id = 0, // ID fake pois LivroCardData usa Int
                            titulo = book?.titulo ?: "Sem Título",
                            autor = book?.autor ?: "Sem Autor",
                            tempo = "Finaliza: $dataLimite",
                            image = book?.imageUrl ?: "" // Pode ser nulo, Glide trata
                         )
                    }
                    adapter.updateData(emprestados)
                }
            } catch (e: Exception) {
                Log.e("HOME_FRAGMENT", "Erro ao carregar emprestimos", e)
                txtNenhumEmprestimo.text = "Erro ao carregar."
                txtNenhumEmprestimo.visibility = View.VISIBLE
            }
        }
    }

    private fun formatarData(dataStr: String?): String {
        if (dataStr == null) return ""
        try {
            // Formato que vem da API: "2025-12-05T23:59:59Z"
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR"))
            val date = inputFormat.parse(dataStr)
            return if (date != null) outputFormat.format(date) else dataStr
        } catch (e: Exception) {
            return dataStr
        }
    }

    private fun carregarLivrosRecentes() {
        lifecycleScope.launch {
            try {
                // Busca os últimos 5 livros (ou qualquer lógica de "recentes")
                val response = livroAPI.getBooks(limit = 5)
                val livros = response.data ?: emptyList()

                val imageViews = listOf(recente1, recente2, recente3, recente4, recente5)

                livros.zip(imageViews).forEach { (livro, imageView) ->
                    if (!livro.imageUrl.isNullOrEmpty()) {
                        Glide.with(this@HomeFragment)
                            .load(livro.imageUrl)
                            .into(imageView)
                    } else {
                         // Placeholder caso não tenha imagem
                         // imageView.setImageResource(...)
                    }

                    imageView.setOnClickListener {
                        verLivro(livro)
                    }
                }
            } catch (e: Exception) {
                Log.e("HOME_FRAGMENT", "Erro ao carregar livros recentes", e)
            }
        }
    }

    fun verLivro(livro: LivroData){
        val fragment = LivroFragment.newInstance(livro)
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("home")
    }
}
