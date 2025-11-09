package com.example.uniforbiblioteca.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uniforbiblioteca.R

class RecuperarActivity : AppCompatActivity() {

    lateinit var voltar: Button
    lateinit var enviar: Button
    lateinit var email: EditText

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

    }

    override fun onStart() {
        super.onStart()

        voltar.setOnClickListener {
            onBackPressed()
        }

        enviar.setOnClickListener {
            val intencao = Intent(this, ModificarActivity::class.java)
            startActivity(intencao)
        }
    }
}