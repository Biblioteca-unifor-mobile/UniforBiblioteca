package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.dataclass.LivroData

class AdminLivroFragment : Fragment() {

    lateinit var editar: Button
    lateinit var exemplares: Button
    private var livro: LivroData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        livro = arguments?.getSerializable("livro") as? LivroData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_admin_livro, container, false)

        editar = view.findViewById(R.id.admin_livro_editar)
        exemplares = view.findViewById(R.id.admin_livro_ver_exemplares)

        livro?.let {
            atualizarCampos(view, it)
        } ?: run {
            Log.d("ADMIN_LIVRO", "⚠️ Nenhum livro recebido!")
        }

        exemplares.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminExemplaresFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        editar.setOnClickListener {
            val fragment = AdminEditLivroFragment.newInstance(livro)
            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
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

        edicaoView.text = "${livro.edicao ?: "—"} / ${livro.anoEdicao ?: "—"}"
        idiomaView.text = livro.idioma ?: "—"
        resumoView.text = livro.resumo ?: "—"
        publicacaoView.text = livro.publicacao ?: "—"

        quantidadeView.text = "Quantidade no acervo: ${livro.copies?.size ?: 0}"
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("livro")
    }

    companion object {
        fun newInstance(livro: LivroData?): AdminLivroFragment {
            val fragment = AdminLivroFragment()
            val args = Bundle()
            args.putSerializable("livro", livro)
            fragment.arguments = args
            return fragment
        }
    }
}
