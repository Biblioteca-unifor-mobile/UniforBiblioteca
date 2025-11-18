package com.example.uniforbiblioteca.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
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
import com.example.uniforbiblioteca.dataclass.VerifyCodeRequest
import kotlinx.coroutines.launch
import java.io.IOException

class CodigoVerificacaoActivity : AppCompatActivity() {

    lateinit var voltarBtn: Button
    private val otpEditTexts = arrayOfNulls<EditText>(6)
    private lateinit var usuarioAPI: UsuarioAPI
    private lateinit var tokenHandler: AuthTokenHandler
    private var userEmail: String? = null
    private lateinit var erroCodigoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_codigo_verificacao)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userEmail = intent.getStringExtra("USER_EMAIL")

        voltarBtn = findViewById(R.id.codigoVerificacaoVoltarBtn)
        erroCodigoTextView = findViewById(R.id.erroCodigoTextView)

        otpEditTexts[0] = findViewById(R.id.otp_edit_text1)
        otpEditTexts[1] = findViewById(R.id.otp_edit_text2)
        otpEditTexts[2] = findViewById(R.id.otp_edit_text3)
        otpEditTexts[3] = findViewById(R.id.otp_edit_text4)
        otpEditTexts[4] = findViewById(R.id.otp_edit_text5)
        otpEditTexts[5] = findViewById(R.id.otp_edit_text6)

        val retrofit = RetrofitClient.create(this)
        usuarioAPI = retrofit.create(UsuarioAPI::class.java)
        tokenHandler = AuthTokenHandler(this)

        setupOtpInputs()
    }

    private fun setupOtpInputs() {
        for (i in 0..5) {
            otpEditTexts[i]?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.isNullOrEmpty()) {
                        if (i < 5) {
                            otpEditTexts[i + 1]?.requestFocus()
                        } else {
                            verifyCode()
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            otpEditTexts[i]?.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && i > 0 && otpEditTexts[i]?.text.isNullOrEmpty()) {
                    otpEditTexts[i - 1]?.requestFocus()
                }
                false
            }
        }
    }

    private fun verifyCode() {
        val otpCode = otpEditTexts.joinToString("") { it?.text.toString() }

        if (otpCode.length == 6 && userEmail != null) {
            lifecycleScope.launch {
                try {
                    Log.d("CodigoVerificacaoActivity", "Verificando código $otpCode para o e-mail: $userEmail")
                    val request = VerifyCodeRequest(userEmail!!, otpCode)
                    val response = usuarioAPI.verifyCode(request)

                    if (response.access_token != null) {
                        tokenHandler.setToken(response.access_token)
                        val intencao = Intent(this@CodigoVerificacaoActivity, ModificarActivity::class.java)
                        startActivity(intencao)
                    } else {
                        showError("Código de verificação inválido.")
                    }

                } catch (e: IOException) {
                    Log.e("CodigoVerificacaoActivity", "Erro de rede", e)
                    showError(e.message)
                } catch (e: Exception) {
                    Log.e("CodigoVerificacaoActivity", "Erro ao verificar o código", e)
                    showError(e.message)
                }
            }
        } else {
            showError("Por favor, insira um código de 6 dígitos")
        }
    }

    private fun showError(message: String?) {
        erroCodigoTextView.text = message ?: "Ocorreu um erro."
        erroCodigoTextView.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()

        voltarBtn.setOnClickListener {
            onBackPressed()
        }
    }
}