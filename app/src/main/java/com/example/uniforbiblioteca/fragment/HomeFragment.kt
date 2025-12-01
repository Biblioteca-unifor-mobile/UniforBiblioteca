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
import com.example.uniforbiblioteca.auth.AuthTokenHandler
import com.example.uniforbiblioteca.api.CartAPI
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.rvadapter.EmprestadoAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class HomeFragment : Fragment() {

    lateinit var nome: TextView

    lateinit var matricula: TextView
    lateinit var sairBtn: Button

    lateinit var recente1: ImageView
    lateinit var recente2: ImageView
    lateinit var recente3: ImageView
    lateinit var recente4: ImageView
    lateinit var recente5: ImageView

    private lateinit var tokenHandler: AuthTokenHandler
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



        val adapter = EmprestadoAdapter(emptyList()){
            Log.d("Home", "aaa")
        }
        recyclerView.adapter = adapter

        sairBtn = view.findViewById(R.id.sairBtn)

        sairBtn.setOnClickListener {
            (activity as? MainActivity)?.sair()
        }

        matricula = view.findViewById(R.id.home_matricula)
        nome = view.findViewById(R.id.home_nome)

        tokenHandler = AuthTokenHandler(requireContext())

        val nomeUsuario = tokenHandler.getName()
        val matriculaUsuario = tokenHandler.getMatricula()

        nome.text = "Olá, ${nomeUsuario ?: "Usuário"}"
        matricula.text = matriculaUsuario ?: "Matrícula não encontrada"

        carregarLivrosRecentes()
        carregarEmprestimos(adapter, recyclerView)

        return view
    }

    private fun carregarEmprestimos(adapter: EmprestadoAdapter, recyclerView: RecyclerView) {
        lifecycleScope.launch {
            try {
                // A API retorna a lista de empréstimos diretamente
                val loans = cartAPI.getMyLoans()
                
                if (loans.isEmpty()) {
                    txtNenhumEmprestimo.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    txtNenhumEmprestimo.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    
                    // Converter Loan para LivroCardData para usar o Adapter existente
                    val emprestados = loans.map { loan ->
                         val dataLimite = formatarData(loan.dataLimite)
                         LivroCardData(
                            id = "0", // ID fake 
                            titulo = loan.bookCopy?.book?.titulo ?: "Sem Título",
                            autor = loan.bookCopy?.book?.autor ?: "Sem Autor",
                            tempo = "Finaliza: $dataLimite",
                            image = loan.bookCopy?.book?.imageUrl ?: "",
                            status = "Emprestado"
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
        if (dataStr.isNullOrEmpty()) return ""
        try {
            var date: java.util.Date? = null
            
            // Tenta primeiro com milissegundos (Formato ISO 8601 completo)
            // Ex: 2025-12-25T18:00:00.000Z
            try {
                val inputFormatMillis = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                inputFormatMillis.timeZone = TimeZone.getTimeZone("UTC")
                date = inputFormatMillis.parse(dataStr)
            } catch (e: Exception) { 
                // Ignora e tenta o próximo formato
            }

            // Se falhar, tenta sem milissegundos
            // Ex: 2025-12-05T23:59:59Z
            if (date == null) {
                val inputFormatNoMillis = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                inputFormatNoMillis.timeZone = TimeZone.getTimeZone("UTC")
                date = inputFormatNoMillis.parse(dataStr)
            }
            
            // Se ainda falhar, poderia tentar outros formatos ou assumir que date é null

            return if (date != null) {
                val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR"))
                outputFormat.format(date)
            } else {
                dataStr // Retorna original se não conseguir formatar
            }
        } catch (e: Exception) {
            Log.e("HOME_FRAGMENT", "Erro na formatação: $dataStr", e)
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
