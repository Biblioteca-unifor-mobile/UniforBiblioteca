package com.example.uniforbiblioteca.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.UsuarioAPI

import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dataclass.Usuario
import kotlinx.coroutines.launch
import androidx.core.content.edit
import com.example.uniforbiblioteca.auth.AuthTokenHandler

class APITestActivity : AppCompatActivity() {

    // 🧠 JWT fixo para testes
    private var jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtYXRyaWN1bGEiOiJBRE1JTjAwMSIsInJvbGUiOiJBRE1JTklTVFJBRE9SIiwibm9tZSI6IkFkbWluIFVzZXIiLCJlbWFpbCI6ImFkbWluQGV4YW1wbGUuY29tIiwiaWF0IjoxNzYyNjQ5Mzg3LCJleHAiOjE3NjI2NjAxODd9.ISxYIX1vIOCQMc3u_ZwCNYbBSNY4TmK8uHQvBjBkQR8"

    // Retrofit já configurado com o token
    private val livroAPI: LivroAPI by lazy {
        RetrofitClient.create(this)
            .create(LivroAPI::class.java)
    }

    private val usuarioAPI: UsuarioAPI by lazy {
        RetrofitClient.create(this)
            .create(UsuarioAPI::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apitest)

        lifecycleScope.launch {
            testAuthRoutes()
        }
    }

    private suspend fun testAuthRoutes(){
        try {
            val newUser = Usuario(
                matricula = "1111111",
                nome = "Joao Victor",
                email = "joaotest1@unifor.br",
                senha = "senhaSegura123"
            )

            val loginUser = Usuario(
                matricula = "1111111",
                senha = "senhaSegura123"
            )

            //var registerResponse = usuarioAPI.register(newUser)

            //Log.d("API_TEST", registerResponse.access_token ?: "sem token do registro")

            var loginResponse = usuarioAPI.login(loginUser)

            Log.d("API_TEST", loginResponse.access_token)

            val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

            val tokenHandler = AuthTokenHandler(this)
            tokenHandler.setToken(loginResponse.access_token)

            Log.d("API_TEST", "ROLE = ${tokenHandler.getRole()}")

            var profile = usuarioAPI.profile()

            Log.d("API_TEST", "ROLE = ${profile.role}")

        } catch (error: Exception) {
            Log.e("API_TEST", "Erro: ${error.message}", error)

        }
    }

    private suspend fun testLivroRoutes() {
        try {
            Log.d("API_TEST", "Iniciando testes com JWT...")

            val novoLivro = LivroData(
                isbn = "000-00-000-0000-0",
                titulo = "Livro Teste",
                autor = "Autor Teste",
                coAutores = listOf("Coautor Teste"),
                edicao = "1ª edição",
                anoEdicao = 2025,
                idioma = "Português",
                publicacao = "Editora Teste",
                resumo = "Resumo teste",
                imageUrl = "https://example.com/imagem.jpg",
                tipo = "FISICO",
                numeroExemplares = 2
            )

            //livroAPI.createBook(novoLivro)
            Log.d("API_TEST", "✅ Livro criado")

            val response = livroAPI.getBooks(page = 1, limit = 10)
            val livros = response.data ?: emptyList()
            Log.d("API_TEST", "📚 Livros retornados: ${livros.size}")

            val livroId = livros.lastOrNull()?.id ?: ""
            if (livroId.isNotEmpty()) {
                val livro = livroAPI.getBook(livroId)
                Log.d("API_TEST", "📖 Livro obtido: ${livro.titulo}")
            }

            if (livros.isNotEmpty()) {
                val livro = LivroData(
                    titulo = "Titulo Atualizado"
                )
                val livroAtualizado = livroAPI.pacthBook("cmhqqyh3l0003tgzs9v05xpai", livro)
                Log.d("API_TEST", "✏️ Livro atualizado: ${livroAtualizado.titulo}")
            }

            if (livroId.isNotEmpty()) {
                val livro = livroAPI.getBook("cmhqqyh3l0003tgzs9v05xpai")
                Log.d("API_TEST", "📖 Livro modificado: ${livro.titulo}")
            }

            if (livroId.isNotEmpty()) {
                livroAPI.deleteBook("cmhqqyh3l0003tgzs9v05xpai")
                Log.d("API_TEST", "🗑️ Livro deletado")
            }

        } catch (e: Exception) {
            Log.e("API_TEST", "❌ Erro: ${e.message}", e)
        }
    }
}