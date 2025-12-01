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
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.ExemplarData
import com.example.uniforbiblioteca.dialog.EstadoExemplarDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExemplarIndisponivelFragment(
    var exemplar: ExemplarData
) : Fragment() {

    private val livroAPI by lazy {
        RetrofitClient.create(context).create(LivroAPI::class.java)
    }

    private lateinit var exemplarIndisponivelCapa: ImageView
    private lateinit var exemplarIndisponivelCondicoes: TextView
    private lateinit var exemplarIndisponivelDataIndisponivel: TextView
    private lateinit var exemplarIndisponivelVerBtn: Button
    private lateinit var exemplarIndisponivelEditarBtn: Button
    private lateinit var exemplarIndisponivelInindisponivelBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exemplar_indisponivel, container, false)

        exemplarIndisponivelCapa = view.findViewById(R.id.exemplar_indisponivel_capa)
        exemplarIndisponivelCondicoes = view.findViewById(R.id.exemplar_indisponivel_condicoes)
        exemplarIndisponivelDataIndisponivel = view.findViewById(R.id.exemplar_indisponivel_data_indisponivel)
        exemplarIndisponivelVerBtn = view.findViewById(R.id.exemplar_indisponivel_ver_btn)
        exemplarIndisponivelEditarBtn = view.findViewById(R.id.exemplar_indisponivel_editar_btn)
        exemplarIndisponivelInindisponivelBtn = view.findViewById(R.id.exemplar_indisponivel_inindisponivel_btn)

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
                .into(exemplarIndisponivelCapa)
        } else {
            exemplarIndisponivelCapa.setImageResource(R.drawable.book_cover_placeholder)
        }

        exemplarIndisponivelCondicoes.text = "Condição: ${exemplar.condition ?: "-"}"
        
        // Mostra a data de hoje formatada como placeholder se não houver dados reais de indisponibilidade
        // Em um cenário real, essa data viria do histórico ou metadados do exemplar
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        val dataHoje = dateFormat.format(Date())
        exemplarIndisponivelDataIndisponivel.text = "Desde: $dataHoje"
    }

    private fun setupListeners() {
        exemplarIndisponivelVerBtn.setOnClickListener {
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

        exemplarIndisponivelEditarBtn.setOnClickListener {
            val dialog = EstadoExemplarDialog { condicao ->
                alterarCondicao(condicao)
            }
            dialog.show(parentFragmentManager, "EstadoExemplar")
        }

        exemplarIndisponivelInindisponivelBtn.setOnClickListener {
            alterarStatus("DISPONIVEL")
        }
    }

    private fun alterarCondicao(novaCondicao: String) {
        val id = exemplar.id ?: return
        lifecycleScope.launch {
            try {
                val payload = ExemplarData(status = exemplar.status, condition = novaCondicao)
                val updated = livroAPI.patchBookCopy(id, payload)
                exemplar = updated
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
                val payload = ExemplarData(status = novoStatus, condition = exemplar.condition)
                livroAPI.patchBookCopy(id, payload)
                Toast.makeText(requireContext(), "Status alterado para $novoStatus", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro ao alterar status", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("Exemplar")
    }
}
