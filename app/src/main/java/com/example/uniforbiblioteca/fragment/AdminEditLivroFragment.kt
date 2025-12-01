package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import com.example.uniforbiblioteca.dataclass.LivroData
import kotlinx.coroutines.launch

class AdminEditLivroFragment : Fragment() {

    private var livro: LivroData? = null

    lateinit var capaImage: ImageView
    lateinit var tituloView: EditText
    lateinit var tipoView: EditText
    lateinit var autorView: EditText
    lateinit var coautorView: EditText
    lateinit var edicaoView: EditText
    lateinit var anoEdicaoView: EditText
    lateinit var idiomaView: EditText
    lateinit var publicacaoView: EditText
    lateinit var resumoView: EditText
    lateinit var isbnView: EditText
    lateinit var exemplaresView: TextView 

    lateinit var addCoautorBtn: Button
    lateinit var procurarBtn: Button

    lateinit var confirmarBtn: Button
    lateinit var cancelarBtn: Button

    private val livroAPI by lazy {
        RetrofitClient.create(context).create(LivroAPI::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        livro = arguments?.getSerializable("livro") as? LivroData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_edit_livro, container, false)

        capaImage = view.findViewById(R.id.admin_edit_livro_capa)

        tituloView = view.findViewById(R.id.admin_edit_livro_titulo_antigo) 
        
        tipoView = view.findViewById(R.id.admin_edit_livro_tipo)
        autorView = view.findViewById(R.id.admin_edit_livro_autor)
        coautorView = view.findViewById(R.id.admin_edit_livro_coautor)
        edicaoView = view.findViewById(R.id.admin_edit_livro_edicao)
        anoEdicaoView = view.findViewById(R.id.admin_edit_livro_ano_edicao)
        idiomaView = view.findViewById(R.id.admin_edit_livro_idioma)
        publicacaoView = view.findViewById(R.id.admin_edit_livro_publicacao)
        resumoView = view.findViewById(R.id.admin_edit_livro_resumo)
        isbnView = view.findViewById(R.id.admin_edit_livro_ISBN)
        exemplaresView = view.findViewById(R.id.admin_edit_livro_exemplares)

        addCoautorBtn = view.findViewById(R.id.add_coautor)
        procurarBtn = view.findViewById(R.id.admin_edit_livro_procurar)
        confirmarBtn = view.findViewById(R.id.admin_edit_livro_confirmar)
        cancelarBtn = view.findViewById(R.id.admin_edit_livro_cancelar)

        livro?.let { preencherCampos(it) }

        cancelarBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        confirmarBtn.setOnClickListener {
            salvarAlteracoes()
        }

        return view
    }

    private fun preencherCampos(livro: LivroData) {
        if (!livro.imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(livro.imageUrl)
                .placeholder(R.drawable.book_cover_placeholder)
                .error(R.drawable.book_cover_placeholder)
                .into(capaImage)
        } else {
            capaImage.setImageResource(R.drawable.book_cover_placeholder)
        }

        tituloView.setText(livro.titulo)
        tipoView.setText(livro.tipo)
        autorView.setText(livro.autor)
        edicaoView.setText(livro.edicao)
        anoEdicaoView.setText(livro.anoEdicao?.toString() ?: "")
        idiomaView.setText(livro.idioma)
        publicacaoView.setText(livro.publicacao)
        resumoView.setText(livro.resumo)
        isbnView.setText(livro.isbn)
        
        val qtd = livro.copies?.size ?: livro.numeroExemplares ?: 0
        exemplaresView.text = qtd.toString()

        val coautores = livro.coAutores ?: emptyList()
        if (coautores.isNotEmpty()) {
            coautorView.setText(coautores[0])
        }
    }

    private fun salvarAlteracoes() {
        val currentLivro = livro ?: return
        val id = currentLivro.id ?: return

        val coautor = coautorView.text.toString().takeUnless { it.isEmpty() }
        val coautores = mutableListOf<String>()
        if (!coautor.isNullOrBlank()){
            coautores.add(coautor)
        }

        // Cria uma cópia do livro atual, mas define id e copies como nulo
        // para evitar erro 400 do backend que não aceita essas propriedades no payload de atualização
        val livroAtualizado = currentLivro.copy(
            id = null,
            copies = null,
            titulo = tituloView.text.toString().takeUnless { it.isEmpty() },
            tipo = tipoView.text.toString().takeUnless { it.isEmpty() },
            autor = autorView.text.toString().takeUnless { it.isEmpty() },
            edicao = edicaoView.text.toString().takeUnless { it.isEmpty() },
            anoEdicao = anoEdicaoView.text.toString().toIntOrNull(),
            idioma = idiomaView.text.toString().takeUnless { it.isEmpty() },
            publicacao = publicacaoView.text.toString().takeUnless { it.isEmpty() },
            resumo = resumoView.text.toString().takeUnless { it.isEmpty() },
            isbn = isbnView.text.toString().takeUnless { it.isEmpty() },
            coAutores = coautores
        )

        lifecycleScope.launch {
            try {
                livroAPI.pacthBook(id, livroAtualizado)
                Toast.makeText(context, "Livro atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(context, "Erro ao atualizar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("Editar Livro")
    }

    companion object {
        fun newInstance(livro: LivroData): AdminEditLivroFragment {
            val fragment = AdminEditLivroFragment()
            val args = Bundle()
            args.putSerializable("livro", livro)
            fragment.arguments = args
            return fragment
        }
    }
}
