package com.example.uniforbiblioteca.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.api.UsuarioAPI
import com.example.uniforbiblioteca.auth.AuthTokenHandler
import com.example.uniforbiblioteca.dataclass.Usuario
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    lateinit var entrar: TextView
    lateinit var registrar: TextView
    lateinit var recuperar: TextView
    lateinit var matricula: EditText
    lateinit var senha: EditText

    private val usuarioAPI: UsuarioAPI by lazy {
        RetrofitClient.create(this)
            .create(UsuarioAPI::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        entrar = findViewById(R.id.entrarBtn)
        recuperar = findViewById(R.id.esqueciSenha)
        registrar = findViewById(R.id.paraRegistroBtn)
        matricula = findViewById(R.id.login_matricula)
        senha = findViewById(R.id.login_senha)
    }

    override fun onStart() {
        super.onStart()

        entrar.setOnClickListener {
            val matriculaStr = matricula.text.toString()
            val senhaStr = senha.text.toString()

            if (matriculaStr.isEmpty() || senhaStr.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha a matrícula e a senha.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (senhaStr.length < 6) {
                Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = Usuario(
                matricula = matriculaStr,
                senha = senhaStr
            )

            lifecycleScope.launch {
                try {
                    val loginResponse = usuarioAPI.login(user)
                    val token = loginResponse.access_token
                    val tokenHandler = AuthTokenHandler(this@LoginActivity)
                    tokenHandler.setToken(token)

                    if (tokenHandler.getRole() == "ADMINISTRADOR") {
                        val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                } catch (e: HttpException) {
                    val errorMessage = when (e.code()) {
                        401 -> "Credenciais inválidas."
                        403 -> "Acesso negado."
                        404 -> "Usuário não encontrado."
                        else -> "Ocorreu um erro: ${e.message()}"
                    }
                    Log.e("LoginActivity", "HTTP error: ${e.code()} ${e.message()}", e)
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: IOException) {
                    Log.e("LoginActivity", "Network error", e)
                    Toast.makeText(this@LoginActivity, "Erro de conexão. Verifique sua internet.", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Login failed", e)
                    Toast.makeText(this@LoginActivity, "Login falhou: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        recuperar.setOnClickListener {
            val intencao = Intent(this, RecuperarActivity::class.java)
            startActivity(intencao)
        }

        registrar.setOnClickListener {
            val intencao = Intent(this, RegistroActivity::class.java)
            startActivity(intencao)
        }
    }
}