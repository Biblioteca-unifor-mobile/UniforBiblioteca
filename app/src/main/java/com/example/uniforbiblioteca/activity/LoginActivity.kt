package com.example.uniforbiblioteca.activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uniforbiblioteca.R

class LoginActivity : AppCompatActivity() {
    lateinit var entrar: TextView
    lateinit var registrar: TextView
    lateinit var recuperar: TextView
    lateinit var matricula: EditText
    lateinit var senha: EditText

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
            if (!matricula.text.isEmpty()) {
                val intencao = Intent(this, AdminActivity::class.java)
                startActivity(intencao)
            } else {
                val intencao = Intent(this, MainActivity::class.java)
                startActivity(intencao)
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