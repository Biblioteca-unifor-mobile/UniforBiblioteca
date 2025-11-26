package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.api.CartAPI
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.CartCheckoutRequest
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dialog.SelecionarPastaDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class LivroFragment : Fragment() {

    private lateinit var addACesta: Button
    private lateinit var addAPasta: Button
    private lateinit var voltar: Button

    private var livro: LivroData? = null
    private var quantidadeDisponivel = 0

    private val livroAPI by lazy {
        RetrofitClient.create(context).create(LivroAPI::class.java)
    }

    private val cartAPI by lazy {
        RetrofitClient.create(context).create(CartAPI::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        livro = arguments?.getSerializable("livro") as? LivroData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_livro, container, false)

        addACesta = view.findViewById(R.id.addaCesta)
        addAPasta = view.findViewById(R.id.addaPasta)
        voltar = view.findViewById(R.id.livro_voltar)

        livro?.let {
            atualizarCampos(view, it)
            it.id?.let { id -> buscarDetalhesLivro(id, view) }
        } ?: run {
            Log.d("LIVRO_FRAGMENT", "⚠️ Nenhum livro recebido!")
        }

        addACesta.setOnClickListener {
            adicionarAoCarrinhoOuReservar()
        }

        addAPasta.setOnClickListener {
            val dialog = SelecionarPastaDialog(requireContext())
            dialog.show()
        }

        voltar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return view
    }

    private fun buscarDetalhesLivro(id: String, view: View) {
        lifecycleScope.launch {
            try {
                val livroAtualizado = livroAPI.getBook(id)
                livro = livroAtualizado
                atualizarCampos(view, livroAtualizado)
            } catch (e: Exception) {
                Log.e("LIVRO_FRAGMENT", "Erro ao buscar detalhes do livro: ${e.message}")
            }
        }
    }

    private fun atualizarCampos(view: View, livro: LivroData) {
        val tipoView = view.findViewById<TextView>(R.id.livro_tipo)
        val tituloView = view.findViewById<TextView>(R.id.livro_titulo)
        val autorView = view.findViewById<TextView>(R.id.livro_autor)
        val coautorView = view.findViewById<TextView>(R.id.livro_coautor)
        val coautor2View = view.findViewById<TextView>(R.id.livro_coautor2)
        val idiomaView = view.findViewById<TextView>(R.id.livro_idioma)
        val resumoView = view.findViewById<TextView>(R.id.livro_resumo)
        val publicacaoView = view.findViewById<TextView>(R.id.livro_publicacao)
        val edicaoView = view.findViewById<TextView>(R.id.livro_edicao)
        val quantiadeView = view.findViewById<TextView>(R.id.livro_quantidade)

        tipoView.text = livro.tipo ?: "—"
        tituloView.text = livro.titulo ?: "—"
        autorView.text = livro.autor ?: "—"

        val coautores = livro.coAutores ?: emptyList()
        if (coautores.isNotEmpty()) {
            coautorView.text = coautores.getOrNull(0) ?: ""
            coautor2View.text = coautores.getOrNull(1) ?: ""
            coautorView.visibility = View.VISIBLE
            coautor2View.visibility = if (coautores.size > 1) View.VISIBLE else View.GONE
        } else {
            coautorView.visibility = View.GONE
            coautor2View.visibility = View.GONE
        }

        idiomaView.text = livro.idioma ?: "—"
        resumoView.text = livro.resumo ?: "—"
        publicacaoView.text = livro.publicacao ?: "—"
        edicaoView.text = "${livro.edicao ?: "—"} / ${livro.anoEdicao ?: "—"}"

        val quantidadeTotal = (livro.copies ?: emptyList()).size
        quantidadeDisponivel = 0
        for (exemplar in livro.copies ?: emptyList()){
            if (exemplar.status == "DISPONIVEL" || exemplar.status == "Disponível"){
                quantidadeDisponivel += 1
            }
        }
        
        val finalTotal = if (quantidadeTotal == 0 && livro.numeroExemplares != null) livro.numeroExemplares else quantidadeTotal

        quantiadeView.text = "Quantidade Disponível: ${quantidadeDisponivel} / ${finalTotal}"

        if (quantidadeDisponivel > 0) {
            addACesta.text = "Adicionar à Cesta"
        } else {
            addACesta.text = "Reservar"
        }
    }

    private fun adicionarAoCarrinhoOuReservar() {
        val currentLivro = livro ?: return

        lifecycleScope.launch {
            try {
                if (quantidadeDisponivel > 0) {
                    // Pegar primeiro exemplar disponível
                    val exemplarDisponivel = currentLivro.copies?.firstOrNull { 
                        it.status == "DISPONIVEL" || it.status == "Disponível" 
                    }
                    
                    if (exemplarDisponivel?.id != null) {
                        cartAPI.addToCart(exemplarDisponivel.id)
                        Toast.makeText(context, "Adicionado à cesta!", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    } else {
                        Toast.makeText(context, "Erro: ID do exemplar não encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Reservar
                    val bookId = currentLivro.id
                    if (bookId != null) {
                        // Data limite padrão (ex: 15 dias)
                        val calendar = Calendar.getInstance()
                        calendar.add(Calendar.DAY_OF_YEAR, 15)
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                        val dataLimite = dateFormat.format(calendar.time)

                        cartAPI.reserveBook(bookId, CartCheckoutRequest(dataLimite))
                        Toast.makeText(context, "Reserva realizada com sucesso!", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    } else {
                         Toast.makeText(context, "Erro: ID do livro não encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("LIVRO_FRAGMENT", "Erro ao adicionar/reservar: ${e.message}")
                Toast.makeText(context, "Erro na operação: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("livro")
    }

    companion object {
        fun newInstance(livro: LivroData): LivroFragment {
            val fragment = LivroFragment()
            val args = Bundle()
            args.putSerializable("livro", livro)
            fragment.arguments = args
            return fragment
        }
    }
}
