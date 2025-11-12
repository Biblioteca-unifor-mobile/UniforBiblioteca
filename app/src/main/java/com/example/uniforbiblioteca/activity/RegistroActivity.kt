package com.example.uniforbiblioteca.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

class RegistroActivity : AppCompatActivity() {

    lateinit var nome: EditText
    lateinit var matricula: EditText
    lateinit var email: EditText
    lateinit var senha: EditText
    lateinit var confirmar: EditText
    lateinit var backBtn: Button
    lateinit var registrarBtn: Button
    lateinit var entrarBtn: TextView

    private val usuarioAPI: UsuarioAPI by lazy {
        RetrofitClient.create(this)
            .create(UsuarioAPI::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nome = findViewById<EditText>(R.id.nomeEditView)
        matricula = findViewById<EditText>(R.id.matriculaEditView)
        email = findViewById<EditText>(R.id.emailEditView)
        senha = findViewById<EditText>(R.id.senhaEditView)
        confirmar = findViewById<EditText>(R.id.confirmarSenhaEditView)

        backBtn = findViewById<Button>(R.id.backBtn)
        registrarBtn = findViewById<Button>(R.id.registrarButton)

        entrarBtn = findViewById<TextView>(R.id.japossuicontaBtn)
    }
    override fun onStart() {
        super.onStart()

        backBtn.setOnClickListener {
            onBackPressed()
        }

        registrarBtn.setOnClickListener {
            val nomeStr = nome.text.toString()
            val matriculaStr = matricula.text.toString()
            val emailStr = email.text.toString()
            val senhaStr = senha.text.toString()
            val confirmarStr = confirmar.text.toString()

            if (nomeStr.isEmpty() || matriculaStr.isEmpty() || emailStr.isEmpty() || senhaStr.isEmpty() || confirmarStr.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (senhaStr != confirmarStr) {
                Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (senhaStr.length < 6) {
                Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newUser = Usuario(
                nome = nomeStr,
                matricula = matriculaStr,
                email = emailStr,
                senha = senhaStr
            )

            lifecycleScope.launch {
                try {
                    val registerResponse = usuarioAPI.register(newUser)
                    val token = registerResponse.access_token
                    val tokenHandler = AuthTokenHandler(this@RegistroActivity)
                    tokenHandler.setToken(token)

                    Toast.makeText(this@RegistroActivity, "Registro bem-sucedido!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RegistroActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: HttpException) {
                    val errorMessage = when (e.code()) {
                        409 -> "Usuário com esta matrícula ou e-mail já existe."
                        400 -> "Dados inválidos. Verifique os campos."
                        else -> "Ocorreu um erro: ${e.message()}"
                    }
                    Log.e("RegistroActivity", "HTTP error: ${e.code()} ${e.message()}", e)
                    Toast.makeText(this@RegistroActivity, errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: IOException) {
                    Log.e("RegistroActivity", "Network error", e)
                    Toast.makeText(this@RegistroActivity, "Erro de conexão. Verifique sua internet.", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Log.e("RegistroActivity", "Registration failed", e)
                    Toast.makeText(this@RegistroActivity, "Registro falhou: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        entrarBtn.setOnClickListener {
            val intencao = Intent(this, LoginActivity::class.java)
            startActivity(intencao)
        }
    }
}