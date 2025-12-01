package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.api.CartAPI
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.ExemplarData
import com.example.uniforbiblioteca.dialog.EstadoExemplarDialog
import kotlinx.coroutines.launch

class ExemplarDisponivelFragment(
    var exemplar: ExemplarData
) : Fragment() {

    private val livroAPI by lazy {
        RetrofitClient.create(context).create(LivroAPI::class.java)
    }

    private val cartAPI by lazy {
        RetrofitClient.create(context).create(CartAPI::class.java)
    }

    private lateinit var exemplarDisponivelCapa: ImageView
    private lateinit var exemplarDisponivelCondicoes: TextView
    private lateinit var exemplarDisponivelUltimoAluguel: TextView
    private lateinit var exemplarDisponivelVerBtn: Button
    private lateinit var exemplarDisponivelEditarBtn: Button
    private lateinit var exemplarDisponivelCancelarBtn: Button
    private lateinit var exemplarDisponivelIndisponivelBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exemplar_disponivel, container, false)

        exemplarDisponivelCapa = view.findViewById(R.id.exemplar_disponivel_capa)
        exemplarDisponivelCondicoes = view.findViewById(R.id.exemplar_disponivel_condicoes)
        exemplarDisponivelUltimoAluguel = view.findViewById(R.id.exemplar_disponivel_ultimo_emprestimo)
        exemplarDisponivelVerBtn = view.findViewById(R.id.exemplar_disponivel_ver_btn)
        exemplarDisponivelEditarBtn = view.findViewById(R.id.exemplar_disponivel_editar_btn)
        exemplarDisponivelCancelarBtn = view.findViewById(R.id.exemplar_disponivel_cesta_btn)
        exemplarDisponivelIndisponivelBtn = view.findViewById(R.id.exemplar_disponivel_indisponivel_btn)

        setupUI()
        setupListeners()

        return view
    }

    private fun setupUI() {
        val book = exemplar.book
        if (book != null && !book.imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(book.imageUrl)
                .placeholder(R.drawable.book_cover_placeholder)
                .error(R.drawable.book_cover_placeholder)
                .into(exemplarDisponivelCapa)
        } else {
            exemplarDisponivelCapa.setImageResource(R.drawable.book_cover_placeholder)
        }

        exemplarDisponivelCondicoes.text = "Condição: ${exemplar.condition ?: "-"}"
        // Como ExemplarData não traz histórico de empréstimos, deixamos um placeholder ou removemos
        exemplarDisponivelUltimoAluguel.text = "Último empréstimo: -"
    }

    private fun setupListeners() {
        exemplarDisponivelVerBtn.setOnClickListener {
            val book = exemplar.book
            if (book != null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.adminFragmentContainer, AdminLivroFragment.newInstance(book))
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(context, "Dados do livro não disponíveis", Toast.LENGTH_SHORT).show()
            }
        }

        exemplarDisponivelEditarBtn.setOnClickListener {
            val dialog = EstadoExemplarDialog { condicao ->
                alterarCondicao(condicao)
            }
            dialog.show(parentFragmentManager, "EstadoExemplar")
        }

        // Botão "Cesta" (conforme ID) - Adicionar à Cesta
        exemplarDisponivelCancelarBtn.setOnClickListener {
            adicionarCesta()
        }

        exemplarDisponivelIndisponivelBtn.setOnClickListener {
            alterarStatus("INDISPONIVEL")
        }
    }

    private fun alterarCondicao(novaCondicao: String) {
        val id = exemplar.id ?: return
        lifecycleScope.launch {
            try {
                val payload = ExemplarData(status = exemplar.status, condition = novaCondicao)
                val updated = livroAPI.patchBookCopy(id, payload)
                exemplar = updated // Atualiza objeto local
                setupUI()
                Toast.makeText(requireContext(), "Exemplar alterado com sucesso", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Não foi possível alterar o exemplar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun alterarStatus(novoStatus: String) {
        val id = exemplar.id ?: return
        lifecycleScope.launch {
            try {
                // Preserva a condição atual ao mudar o status
                val payload = ExemplarData(status = novoStatus, condition = exemplar.condition)
                livroAPI.patchBookCopy(id, payload)
                Toast.makeText(requireContext(), "Status alterado para $novoStatus", Toast.LENGTH_SHORT).show()
                // Volta para a tela anterior pois o status mudou (não está mais "Disponível" no contexto deste fragmento)
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro ao alterar status", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun adicionarCesta() {
        val bookId = exemplar.bookId ?: return
        lifecycleScope.launch {
            try {
                cartAPI.addToCart(bookId)
                Toast.makeText(requireContext(), "Livro adicionado à cesta!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro ao adicionar à cesta: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("Exemplar")
    }
}
