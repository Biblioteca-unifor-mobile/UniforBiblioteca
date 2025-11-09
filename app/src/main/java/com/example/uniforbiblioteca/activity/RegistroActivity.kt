package com.example.uniforbiblioteca.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uniforbiblioteca.R

class RegistroActivity : AppCompatActivity() {

    lateinit var nome: EditText
    lateinit var matricula: EditText
    lateinit var email: EditText
    lateinit var senha: EditText
    lateinit var confirmar: EditText
    lateinit var backBtn: Button
    lateinit var registrarBtn: Button
    lateinit var entrarBtn: TextView

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
            val intencao = Intent(this, LoginActivity::class.java)
            startActivity(intencao)
        }

        entrarBtn.setOnClickListener {
            val intencao = Intent(this, LoginActivity::class.java)
            startActivity(intencao)
        }
    }
}