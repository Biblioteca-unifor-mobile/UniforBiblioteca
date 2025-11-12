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
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dialog.SelecionarPastaDialog

class LivroFragment : Fragment() {

    private lateinit var addACesta: Button
    private lateinit var addAPasta: Button
    private lateinit var voltar: Button

    private var livro: LivroData? = null

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
        } ?: run {
            Log.d("LIVRO_FRAGMENT", "⚠️ Nenhum livro recebido!")
        }

        addACesta.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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
        var quantidadeDisponivel = 0
        for (exemplar in livro.copies ?: emptyList()){
            if (exemplar.status == "DISPONIVEL"){
                quantidadeDisponivel += 1
            }
        }

        quantiadeView.text = "Quantidade Disponível: ${quantidadeDisponivel} / ${quantidadeTotal}"

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
