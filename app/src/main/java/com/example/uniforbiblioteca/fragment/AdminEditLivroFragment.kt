package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.ExemplarData
import com.example.uniforbiblioteca.dataclass.LivroData
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName

class AdminEditLivroFragment : Fragment() {

    private val livroAPI by lazy {
        RetrofitClient.create(requireContext()).create(LivroAPI::class.java)
    }
    private var livro: LivroData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recupera o argumento enviado
        livro = arguments?.getSerializable("livro") as? LivroData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_edit_livro, container, false)

        val capaImage: ImageView = view.findViewById(R.id.admin_edit_livro_capa)
        val titulo: TextView = view.findViewById(R.id.admin_edit_livro_titulo)
        val tipo: EditText = view.findViewById(R.id.admin_edit_livro_tipo)
        val tituloAntigo: EditText = view.findViewById(R.id.admin_edit_livro_titulo_antigo)
        val autor: EditText = view.findViewById(R.id.admin_edit_livro_autor)
        val coautor: EditText = view.findViewById(R.id.admin_edit_livro_coautor)
        val edicao: EditText = view.findViewById(R.id.admin_edit_livro_edicao)
        val anoEdicao: EditText = view.findViewById(R.id.admin_edit_livro_ano_edicao)
        val idioma: EditText = view.findViewById(R.id.admin_edit_livro_idioma)
        val publicacao: EditText = view.findViewById(R.id.admin_edit_livro_publicacao)
        val resumo: EditText = view.findViewById(R.id.admin_edit_livro_resumo)
        val isbn: EditText = view.findViewById(R.id.admin_edit_livro_ISBN)

        val addCoautorBtn: Button = view.findViewById(R.id.add_coautor)
        val procurarBtn: Button = view.findViewById(R.id.admin_edit_livro_procurar)
        val editarBtn: Button = view.findViewById(R.id.admin_edit_livro_confirmar)
        val cancelarBtn: Button = view.findViewById(R.id.admin_edit_livro_cancelar)

        editarBtn.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val novoLivro = LivroData(
                        isbn = isbn.text.toString(),
                        titulo = tituloAntigo.text.toString(),
                        autor = autor.text.toString(),
                        edicao = edicao.text.toString(),
                        anoEdicao = anoEdicao.text.toString().toInt(),
                        idioma = idioma.text.toString(),
                        publicacao = publicacao.text.toString(),
                        resumo = resumo.text.toString(),
                        tipo = tipo.text.toString(),
                    )
                    edit(novoLivro)
                    Log.d("EDIT_LIVRO", "livro editado")

                } catch (e: Exception) {
                    Log.e("EDIT_LIVRO", "erro ao ditar o livro: ${e.message}")
                }
            }
        }
        cancelarBtn.setOnClickListener { parentFragmentManager.popBackStack() }

        livro?.let { l ->
            titulo.text = l.titulo
            tipo.setText(l.tipo ?: "")
            tituloAntigo.setText(l.titulo)
            autor.setText(l.autor ?: "")
            if (l.coAutores?.isNotEmpty() == true){
                coautor.setText((l.coAutores as List<String?>).firstOrNull())
            }
            edicao.setText(l.edicao?.toString() ?: "")
            anoEdicao.setText(l.anoEdicao?.toString() ?: "")
            idioma.setText(l.idioma ?: "")
            publicacao.setText(l.publicacao ?: "")
            resumo.setText(l.resumo ?: "")
            isbn.setText(l.isbn ?: "")

            l.imageUrl?.takeIf { it.isNotBlank() }?.let { url ->
                Glide.with(this).load(url).into(capaImage)
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("Edit Livro")
    }

    suspend fun edit(novoLivro: LivroData){
        val livroAtualizado = livroAPI.pacthBook(livro?.id, novoLivro)
        Toast.makeText(context, "Livro atualizado", Toast.LENGTH_LONG).show()
        livro?.tipo = livroAtualizado.tipo
        livro?.titulo = livroAtualizado.titulo
        livro?.imageUrl = livroAtualizado.imageUrl
        livro?.autor = livroAtualizado.autor
        livro?.coAutores = livroAtualizado.coAutores
        livro?.copies = livroAtualizado.copies
        livro?.resumo = livroAtualizado.resumo
        livro?.isbn = livroAtualizado.isbn
        livro?.edicao = livroAtualizado.edicao
        livro?.anoEdicao = livroAtualizado.anoEdicao
        livro?.publicacao= livroAtualizado.publicacao
        val fragment = AdminLivroFragment.newInstance(livro)
        parentFragmentManager.beginTransaction()
            .replace(R.id.adminFragmentContainer, fragment)
            .commit()
    }

    companion object {
        fun newInstance(livro: LivroData?): AdminEditLivroFragment {
            val fragment = AdminEditLivroFragment()
            val args = Bundle()
            args.putSerializable("livro", livro)
            fragment.arguments = args
            return fragment
        }
    }
}
