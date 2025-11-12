package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.api.UsuarioAPI
import com.example.uniforbiblioteca.auth.AuthTokenHandler
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dataclass.Usuario
import kotlinx.coroutines.launch

class AdminNovoLivroFragment : Fragment() {

    lateinit var capaImage: ImageView
    lateinit var tituloView: TextView
    lateinit var tipoView: EditText
    lateinit var autorView: EditText
    lateinit var coautorView: EditText
    lateinit var edicaoView: EditText
    lateinit var anoEdicaoView: EditText
    lateinit var idiomaView: EditText
    lateinit var publicacaoView: EditText
    lateinit var resumoView: EditText
    lateinit var isbnView: EditText
    lateinit var exemplaresView: EditText

    lateinit var addCoautorBtn: Button
    lateinit var procurarBtn: Button

    lateinit var confirmarBtn: Button
    lateinit var cancelarBtn: Button

    private val livroAPI: LivroAPI by lazy {
        RetrofitClient.create(context)
            .create(LivroAPI::class.java)
    }

    private val usuarioAPI: UsuarioAPI by lazy {
        RetrofitClient.create(context)
            .create(UsuarioAPI::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_novo_livro, container, false)

        capaImage = view.findViewById(R.id.admin_novo_livro_capa)

        tituloView = view.findViewById(R.id.admin_novo_livro_titulo)
        tipoView = view.findViewById(R.id.admin_novo_livro_tipo)
        autorView = view.findViewById(R.id.admin_novo_livro_autor)
        coautorView = view.findViewById(R.id.admin_novo_livro_coautor)
        edicaoView = view.findViewById(R.id.admin_novo_livro_edicao)
        anoEdicaoView = view.findViewById(R.id.admin_novo_livro_ano_edicao)
        idiomaView = view.findViewById(R.id.admin_novo_livro_idioma)
        publicacaoView = view.findViewById(R.id.admin_novo_livro_publicacao)
        resumoView = view.findViewById(R.id.admin_novo_livro_resumo)
        isbnView = view.findViewById(R.id.admin_novo_livro_ISBN)
        exemplaresView = view.findViewById(R.id.admin_novo_livro_exemplares)


        addCoautorBtn = view.findViewById(R.id.add_coautor)
        procurarBtn = view.findViewById(R.id.admin_novo_livro_procurar)
        confirmarBtn = view.findViewById(R.id.admin_novo_livro_confirmar)
        cancelarBtn = view.findViewById(R.id.admin_novo_livro_cancelar)


        cancelarBtn.setOnClickListener { parentFragmentManager.popBackStack() }

        lifecycleScope.launch {

            try {
                val loginUser = Usuario(
                    matricula = "ADMIN001",
                    senha = "admin123"
                )
                val loginResponse = usuarioAPI.login(loginUser)

                Log.d("API_TEST", loginResponse.access_token)

                val tokenHandler = AuthTokenHandler(context)
                tokenHandler.setToken(loginResponse.access_token)
            } catch (e: Exception) {
                Log.e("NOVO_LIVRO", "Erro ao fazer login ${e.message}")
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmarBtn.setOnClickListener {


            val coautor = coautorView.text.toString().takeUnless { it.isEmpty() }
            val coautores = mutableListOf<String>()
            if (!coautor.isNullOrBlank()){
                coautores.add(coautor)
            }


            val novolivro = LivroData(
                titulo = tituloView.text.toString().takeUnless { it.isEmpty() },
                tipo = tipoView.text.toString().takeUnless { it.isEmpty() },
                autor = autorView.text.toString().takeUnless { it.isEmpty() },
                edicao = edicaoView.text.toString().takeUnless { it.isEmpty() },
                anoEdicao = anoEdicaoView.text.toString().toIntOrNull(),
                idioma = idiomaView.text.toString().takeUnless { it.isEmpty() },
                publicacao = publicacaoView.text.toString().takeUnless { it.isEmpty() },
                resumo = resumoView.text.toString().takeUnless { it.isEmpty() },
                isbn = isbnView.text.toString().takeUnless { it.isEmpty() },
                numeroExemplares = exemplaresView.text.toString().toIntOrNull() ?: 0,
            )

            lifecycleScope.launch {

                try {

                    livroAPI.createBook(novolivro)
                    val fragment = AdminLivroFragment.newInstance(novolivro)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.adminFragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit()
                } catch (e: Exception){
                    Toast.makeText(context, "Erro ao criar livro", Toast.LENGTH_LONG).show()
                    Log.e("NOVO_LIVRO", "Erro ao criar livro ${e.message}")
                }
            }

        }

    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("Novo Livro")
    }
}
