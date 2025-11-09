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

class ModificarActivity : AppCompatActivity() {

    lateinit var voltar: Button
    lateinit var novaSenha: EditText
    lateinit var confirmarNovaSenha: EditText
    lateinit var modificar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modificar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        voltar = findViewById<Button>(R.id.modificarVoltarBtn)
        novaSenha = findViewById<EditText>(R.id.novaSenhaEditText)
        confirmarNovaSenha = findViewById<EditText>(R.id.confirmarNovaSenhaEditText)
        modificar = findViewById<Button>(R.id.modificarBtn)
    }

    override fun onStart() {
        super.onStart()

        voltar.setOnClickListener {
            onBackPressed()
        }

        modificar.setOnClickListener {
            val intencao = Intent(this, LoginActivity::class.java)
            startActivity(intencao)
        }
    }
}