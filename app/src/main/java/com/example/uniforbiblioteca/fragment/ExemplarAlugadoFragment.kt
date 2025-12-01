package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ExemplarAlugadoFragment(
    var exemplar: ExemplarData
) : Fragment() {

    private val livroAPI by lazy {
        RetrofitClient.create(context).create(LivroAPI::class.java)
    }

    private val cartAPI by lazy {
        RetrofitClient.create(context).create(CartAPI::class.java)
    }

    private lateinit var exemplarAlugadoCapa: ImageView
    private lateinit var exemplarAlugadoNome: TextView
    private lateinit var exemplarAlugadoCondicoes: TextView
    private lateinit var exemplarAlugadoDataEmprestimo: TextView
    private lateinit var exemplarAlugadoDataLimite: TextView
    private lateinit var exemplarAlugadoRenovacoes: TextView
    private lateinit var exemplarAlugadoDivida: TextView
    private lateinit var exemplarAlugadoVerBtn: Button
    private lateinit var exemplarAlugadoEditarBtn: Button
    private lateinit var exemplarAlugadoRenovarBtn: Button
    private lateinit var exemplarAlugadoDevolverBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exemplar_alugado, container, false)

        exemplarAlugadoCapa = view.findViewById(R.id.exemplar_alugado_capa)
        exemplarAlugadoNome = view.findViewById(R.id.exemplar_alugado_nome)
        exemplarAlugadoCondicoes = view.findViewById(R.id.exemplar_alugado_condicoes)
        exemplarAlugadoDataEmprestimo = view.findViewById(R.id.exemplar_alugado_data_emprestimo)
        exemplarAlugadoDataLimite = view.findViewById(R.id.exemplar_alugado_data_limite)
        exemplarAlugadoRenovacoes = view.findViewById(R.id.exemplar_alugado_renovacoes)
        exemplarAlugadoDivida = view.findViewById(R.id.exemplar_alugado_divida)
        exemplarAlugadoVerBtn = view.findViewById(R.id.exemplar_alugado_ver_btn)
        exemplarAlugadoEditarBtn = view.findViewById(R.id.exemplar_alugado_editar_btn)
        exemplarAlugadoRenovarBtn = view.findViewById(R.id.exemplar_alugado_renovar_btn)
        exemplarAlugadoDevolverBtn = view.findViewById(R.id.exemplar_alugado_devolver_btn)

        setupUI()
        setupListeners()
        carregarDadosEmprestimo()

        return view
    }

    private fun setupUI() {
        val book = exemplar.book
        if (book != null) {
            // Aqui podemos exibir "Usuário: Nome" se o layout tiver essa intenção,
            // mas inicialmente exibimos o título do livro na imagem se necessário ou apenas carregamos a imagem.
            // O layout parece ter "Usuário: [Nome]" e a imagem do livro.
            // Se o TextView exemplarAlugadoNome for para o nome do usuário, devemos esperar o carregamento do empréstimo.
            // Se for para o título do livro, ok.
            // Vou assumir que é para o título do livro por enquanto, mas atualizarei com o nome do aluno se disponível no empréstimo.
            exemplarAlugadoNome.text = book.titulo ?: "Sem título"
            
            if (!book.imageUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(book.imageUrl)
                    .placeholder(R.drawable.book_cover_placeholder)
                    .error(R.drawable.book_cover_placeholder)
                    .into(exemplarAlugadoCapa)
            } else {
                exemplarAlugadoCapa.setImageResource(R.drawable.book_cover_placeholder)
            }
        } else {
            exemplarAlugadoCapa.setImageResource(R.drawable.book_cover_placeholder)
            exemplarAlugadoNome.text = "Carregando..."
        }

        exemplarAlugadoCondicoes.text = "Condição: ${exemplar.condition ?: "-"}"
        
        // Define valores iniciais para evitar "TextView" enquanto carrega
        exemplarAlugadoDataEmprestimo.text = "-"
        exemplarAlugadoDataLimite.text = "-"
        exemplarAlugadoRenovacoes.text = "-"
        exemplarAlugadoDivida.text = "-"
    }

    private fun carregarDadosEmprestimo() {
        val copyId = exemplar.id ?: return
        lifecycleScope.launch {
            try {
                // Agora retorna um objeto CopyDetailsResponse contendo emprestimoAtual
                val details = cartAPI.getLoansByCopyId(copyId)
                val activeLoan = details.emprestimoAtual
                
                if (activeLoan != null) {
                    // Atualiza nome do aluno se disponível
                    activeLoan.aluno?.let { aluno ->
                        exemplarAlugadoNome.text = aluno.nome ?: "Aluno Desconhecido"
                    }

                    exemplarAlugadoDataEmprestimo.text = formatarData(activeLoan.dataEmprestimo)
                    exemplarAlugadoDataLimite.text = formatarData(activeLoan.dataLimite)
                    
                    // Campos que podem não existir no LoanDetailsWrapper
                    // Se não existirem, deixamos "-" ou valor padrão
                    // O log mostrava "diasAtraso", mas não renovacoes/divida.
                    // Se o objeto LoanDetailsWrapper não tem esses campos, não podemos exibir.
                    // Vou deixar "-" se não tiver acesso.
                    exemplarAlugadoRenovacoes.text = "-" 
                    exemplarAlugadoDivida.text = "-" 
                } else {
                    exemplarAlugadoDataEmprestimo.text = "-"
                    exemplarAlugadoDataLimite.text = "-"
                }
            } catch (e: Exception) {
                Log.e("EXEMPLAR_ALUGADO", "Erro ao carregar dados do empréstimo", e)
                exemplarAlugadoDataEmprestimo.text = "Erro"
                exemplarAlugadoDataLimite.text = "Erro"
            }
        }
    }

    private fun formatarData(dataStr: String?): String {
        if (dataStr.isNullOrEmpty()) return "-"
        try {
            val formats = listOf(
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") },
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }
            )
            
            var date: java.util.Date? = null
            for (format in formats) {
                try {
                    date = format.parse(dataStr)
                    if (date != null) break
                } catch (e: Exception) { continue }
            }

            return if (date != null) {
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
                outputFormat.format(date)
            } else {
                dataStr
            }
        } catch (e: Exception) {
            return dataStr
        }
    }

    private fun setupListeners() {
        exemplarAlugadoVerBtn.setOnClickListener {
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

        exemplarAlugadoEditarBtn.setOnClickListener {
            val dialog = EstadoExemplarDialog { condicao ->
                alterarCondicao(condicao)
            }
            dialog.show(parentFragmentManager, "EstadoExemplar")
        }

        exemplarAlugadoRenovarBtn.setOnClickListener {
            Toast.makeText(context, "Funcionalidade de renovação em desenvolvimento", Toast.LENGTH_SHORT).show()
        }

        exemplarAlugadoDevolverBtn.setOnClickListener {
            Toast.makeText(context, "Funcionalidade de devolução em desenvolvimento", Toast.LENGTH_SHORT).show()
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

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("Exemplar")
    }
}
