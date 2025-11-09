package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity

class AdminNovoLivroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_novo_livro, container, false)

        val capaImage: ImageView = view.findViewById(R.id.admin_novo_livro_capa)

        val titulo: TextView = view.findViewById(R.id.admin_novo_livro_titulo)
        val tipo: EditText = view.findViewById(R.id.admin_novo_livro_tipo)
        val autor: EditText = view.findViewById(R.id.admin_novo_livro_autor)
        val coautor: EditText = view.findViewById(R.id.admin_novo_livro_coautor)
        val edicao: EditText = view.findViewById(R.id.admin_novo_livro_edicao)
        val anoEdicao: EditText = view.findViewById(R.id.admin_novo_livro_ano_edicao)
        val idioma: EditText = view.findViewById(R.id.admin_novo_livro_idioma)
        val publicacao: EditText = view.findViewById(R.id.admin_novo_livro_publicacao)
        val resumo: EditText = view.findViewById(R.id.admin_novo_livro_resumo)
        val isbn: EditText = view.findViewById(R.id.admin_novo_livro_ISBN)
        val exemplares: EditText = view.findViewById(R.id.admin_novo_livro_exemplares)


        val addCoautorBtn: Button = view.findViewById(R.id.add_coautor)
        val procurarBtn: Button = view.findViewById(R.id.admin_novo_livro_procurar)
        val editarBtn: Button = view.findViewById(R.id.admin_novo_livro_confirmar)
        val cancelarBtn: Button = view.findViewById(R.id.admin_novo_livro_cancelar)

        editarBtn.setOnClickListener { parentFragmentManager.popBackStack() }
        cancelarBtn.setOnClickListener { parentFragmentManager.popBackStack() }

        return view
    }
    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("Novo Livro")
    }
}
