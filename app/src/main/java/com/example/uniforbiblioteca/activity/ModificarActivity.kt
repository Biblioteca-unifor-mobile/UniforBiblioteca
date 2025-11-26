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
import com.example.uniforbiblioteca.dataclass.ResetPasswordRequest
import kotlinx.coroutines.launch
import java.io.IOException

class ModificarActivity : AppCompatActivity() {

    private lateinit var voltar: Button
    private lateinit var novaSenhaEditText: EditText
    private lateinit var confirmarNovaSenhaEditText: EditText
    private lateinit var modificarBtn: Button
    private lateinit var usuarioAPI: UsuarioAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modificar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        voltar = findViewById(R.id.modificarVoltarBtn)
        novaSenhaEditText = findViewById(R.id.novaSenhaEditText)
        confirmarNovaSenhaEditText = findViewById(R.id.confirmarNovaSenhaEditText)
        modificarBtn = findViewById(R.id.modificarBtn)

        val retrofit = RetrofitClient.create(this)
        usuarioAPI = retrofit.create(UsuarioAPI::class.java)
    }

    override fun onStart() {
        super.onStart()

        voltar.setOnClickListener {
            onBackPressed()
        }

        modificarBtn.setOnClickListener {
            val novaSenha = novaSenhaEditText.text.toString()
            val confirmarSenha = confirmarNovaSenhaEditText.text.toString()

            if (novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (novaSenha != confirmarSenha) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    Log.d("ModificarActivity", "Enviando nova senha...")
                    val request = ResetPasswordRequest(novaSenha)
                    val response = usuarioAPI.resetPassword(request)

                    Toast.makeText(this@ModificarActivity, response.message, Toast.LENGTH_SHORT).show()

                    val intencao = Intent(this@ModificarActivity, LoginActivity::class.java)
                    intencao.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intencao)

                } catch (e: IOException) {
                    Log.e("ModificarActivity", "Erro de rede", e)
                    Toast.makeText(this@ModificarActivity, "Erro de rede: ${e.message}", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("ModificarActivity", "Erro ao redefinir senha", e)
                    Toast.makeText(this@ModificarActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}