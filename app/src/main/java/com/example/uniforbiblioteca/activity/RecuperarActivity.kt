package com.example.uniforbiblioteca.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.api.UsuarioAPI
import com.example.uniforbiblioteca.dataclass.ForgotPasswordRequest
import kotlinx.coroutines.launch
import java.io.IOException

class RecuperarActivity : AppCompatActivity() {

    lateinit var voltar: Button
    lateinit var enviar: Button
    lateinit var email: EditText
    private lateinit var usuarioAPI: UsuarioAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        voltar = findViewById<Button>(R.id.backRecuperarbtn)
        enviar = findViewById<Button>(R.id.recuperarBtn)
        email = findViewById<EditText>(R.id.emailRecuperarEditView)

        val retrofit = RetrofitClient.create(this)
        usuarioAPI = retrofit.create(UsuarioAPI::class.java)

    }

    override fun onStart() {
        super.onStart()

        voltar.setOnClickListener {
            onBackPressed()
        }

        enviar.setOnClickListener {
            val userEmail = email.text.toString()
            if (userEmail.isEmpty()) {
                Toast.makeText(this, "Por favor, insira seu e-mail", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    Log.d("RecuperarActivity", "Enviando solicitação de recuperação de senha para: $userEmail")
                    val request = ForgotPasswordRequest(userEmail)
                    val response = usuarioAPI.forgotPassword(request)

                    Toast.makeText(this@RecuperarActivity, response.message, Toast.LENGTH_SHORT).show()

                    val intencao = Intent(this@RecuperarActivity, CodigoVerificacaoActivity::class.java)
                    intencao.putExtra("USER_EMAIL", userEmail)
                    startActivity(intencao)

                } catch (e: IOException) {
                    Log.e("RecuperarActivity", "Erro de rede", e)
                    Toast.makeText(this@RecuperarActivity, "Erro de rede: ${e.message}", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("RecuperarActivity", "Erro ao solicitar recuperação", e)
                    Toast.makeText(this@RecuperarActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}