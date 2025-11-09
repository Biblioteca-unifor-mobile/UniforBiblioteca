package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.LivroData
import kotlinx.coroutines.launch

class AdminLivroFragment : Fragment() {

    lateinit var editar: Button
    lateinit var exemplares: Button
    private var livroId: String? = null
    private val jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." // seu token

    private val livroAPI: LivroAPI by lazy {
        RetrofitClient.create(context)
            .create(LivroAPI::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        livroId = arguments?.getString("livroId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_admin_livro, container, false)

        editar = view.findViewById(R.id.admin_livro_editar)
        exemplares = view.findViewById(R.id.admin_livro_ver_exemplares)

        lifecycleScope.launch {
            livroId?.let { id ->
                val livroData = carregarLivro(id)
                if (livroData != null) {
                    atualizarCampos(view, livroData)
                } else {
                    Log.d("LIVRO_ADMIN", "⚠️ Nenhum livro encontrado com ID $id")
                }
            }
        }

        exemplares.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminExemplaresFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        editar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminEditLivroFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private suspend fun carregarLivro(livroId: String): LivroData? {
        return try {
            livroAPI.getBook(livroId)
        } catch (error: Exception) {
            Log.d("LIVRO_ADMIN", "❌ Erro: ${error.message}")
            null
        }
    }

    private fun atualizarCampos(view: View, livro: LivroData) {

        val tipoView = view.findViewById<TextView>(R.id.admin_livro_tipo)
        val tituloView = view.findViewById<TextView>(R.id.admin_livro_titulo)
        val autorView = view.findViewById<TextView>(R.id.admin_livro_autor)
        val coautorView = view.findViewById<TextView>(R.id.admin_livro_coautor)
        val coautor2View = view.findViewById<TextView>(R.id.admin_livro_coautor2)
        val edicaoView = view.findViewById<TextView>(R.id.admin_livro_edicao)
        val idiomaView = view.findViewById<TextView>(R.id.admin_livro_idioma)
        val resumoView = view.findViewById<TextView>(R.id.admin_livro_resumo)
        val quantidadeView = view.findViewById<TextView>(R.id.admin_livro_quantidade)
        val publicacaoView = view.findViewById<TextView>(R.id.admin_livro_publicacao)

        tipoView.text = livro.tipo ?: "Placeholder"
        tituloView.text = livro.titulo ?: "Placeholder"
        autorView.text = livro.autor ?: "Placeholder"

        val coautores = livro.coAutores ?: emptyList()

        if (coautores.isNotEmpty()) {
            coautorView.text = coautores.getOrNull(0) ?: ""
            coautorView.visibility = View.VISIBLE

            coautor2View.text = coautores.getOrNull(1) ?: ""
            coautor2View.visibility = if (coautores.size > 1) View.VISIBLE else View.GONE
        } else {
            coautorView.visibility = View.GONE
            coautor2View.visibility = View.GONE
        }

        val edicao = livro.edicao ?: "—"
        val ano = livro.anoEdicao ?: "—"
        edicaoView.text = "$edicao / $ano"

        idiomaView.text = livro.idioma ?: "—"
        resumoView.text = livro.resumo ?: "—"
        publicacaoView.text = livro.publicacao ?: "—"

        val copies = livro.copies ?: emptyList()
        quantidadeView.text = "Quantidade no acervo: ${copies.size}"
    }


    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("livro")
    }

    companion object {
        fun newInstance(livroId: String): AdminLivroFragment {
            val fragment = AdminLivroFragment()
            val args = Bundle()
            args.putString("livroId", livroId)
            fragment.arguments = args
            return fragment
        }
    }
}
