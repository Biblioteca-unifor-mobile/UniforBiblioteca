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

class ExemplarReservadoFragment(
    var exemplar: ExemplarData
) : Fragment() {

    private val livroAPI by lazy {
        RetrofitClient.create(context).create(LivroAPI::class.java)
    }

    private lateinit var exemplarReservadoCapa: ImageView
    private lateinit var exemplarReservadoNome: TextView
    private lateinit var exemplarReservadoCondicoes: TextView
    private lateinit var exemplarReservadoDataEmprestimo: TextView
    private lateinit var exemplarReservadoDataLimite: TextView
    private lateinit var exemplarReservadoVerBtn: Button
    private lateinit var exemplarReservadoEditarBtn: Button
    private lateinit var exemplarReservadoCancelarBtn: Button
    private lateinit var exemplarReservadoIndisponivelBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exemplar_reservado, container, false)

        exemplarReservadoCapa = view.findViewById(R.id.exemplar_reservado_capa)
        exemplarReservadoNome = view.findViewById(R.id.exemplar_reservado_nome)
        exemplarReservadoCondicoes = view.findViewById(R.id.exemplar_reservado_condicoes)
        exemplarReservadoDataLimite = view.findViewById(R.id.exemplar_reservado_data_limite)
        exemplarReservadoDataEmprestimo = view.findViewById(R.id.exemplar_reservado_data_reserva)
        exemplarReservadoVerBtn = view.findViewById(R.id.exemplar_reservado_ver_btn)
        exemplarReservadoEditarBtn = view.findViewById(R.id.exemplar_reservado_editar_btn)
        exemplarReservadoCancelarBtn = view.findViewById(R.id.exemplar_reservado_cancelar_btn)
        exemplarReservadoIndisponivelBtn = view.findViewById(R.id.exemplar_reservado_indisponivel_btn)

        setupUI()
        setupListeners()

        return view
    }

    private fun setupUI() {
        val book = exemplar.book
        if (book != null) {
            exemplarReservadoNome.text = book.titulo ?: "Sem título"
            if (!book.imageUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(book.imageUrl)
                    .placeholder(R.drawable.book_cover_placeholder)
                    .error(R.drawable.book_cover_placeholder)
                    .into(exemplarReservadoCapa)
            } else {
                exemplarReservadoCapa.setImageResource(R.drawable.book_cover_placeholder)
            }
        } else {
            exemplarReservadoCapa.setImageResource(R.drawable.book_cover_placeholder)
            exemplarReservadoNome.text = "Dados do livro não disponíveis"
        }

        exemplarReservadoCondicoes.text = "Condição: ${exemplar.condition ?: "-"}"
        
        // Dados específicos da reserva não estão disponíveis no ExemplarData
        exemplarReservadoDataEmprestimo.text = "Data Reserva: -"
        exemplarReservadoDataLimite.text = "Expira em: -"
    }

    private fun setupListeners() {
        exemplarReservadoVerBtn.setOnClickListener {
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

        exemplarReservadoEditarBtn.setOnClickListener {
            val dialog = EstadoExemplarDialog { condicao ->
                alterarCondicao(condicao)
            }
            dialog.show(parentFragmentManager, "EstadoExemplar")
        }

        exemplarReservadoCancelarBtn.setOnClickListener {
            // Cancelar reserva liberando o exemplar (tornando disponível)
            alterarStatus("DISPONIVEL")
        }

        exemplarReservadoIndisponivelBtn.setOnClickListener {
            alterarStatus("INDISPONIVEL")
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
