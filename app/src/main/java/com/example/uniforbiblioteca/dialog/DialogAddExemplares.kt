package com.example.uniforbiblioteca.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.ExemplarData
import kotlinx.coroutines.launch

class DialogAddExemplares : DialogFragment() {

    private lateinit var tituloTextView: TextView
    private lateinit var menosExemplarButton: ImageButton
    private lateinit var maisExemplarButton: ImageButton
    private lateinit var quantidadeEditText: EditText
    private lateinit var confirmarButton: Button
    private lateinit var cancelarButton: Button
    // Opcional: Adicionar ProgressBar no layout se desejar, ou apenas bloquear botões
    
    private val livroAPI by lazy {
        RetrofitClient.create(context).create(LivroAPI::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.dialog_add_exemplares, null)

        val bookId = arguments?.getString("bookId")

        // Inicializa views
        tituloTextView = view.findViewById(R.id.textView59)
        menosExemplarButton = view.findViewById(R.id.menos_exemplar)
        maisExemplarButton = view.findViewById(R.id.mais_exemplar)
        quantidadeEditText = view.findViewById(R.id.quantidade_adicionar)
        confirmarButton = view.findViewById(R.id.confirmar_add_exemplares)
        cancelarButton = view.findViewById(R.id.cancelar_add_exemplares)

        // Listeners
        menosExemplarButton.setOnClickListener {
            var qtd = quantidadeEditText.text.toString().toIntOrNull() ?: 1
            if (qtd > 1) {
                qtd--
                quantidadeEditText.setText(qtd.toString())
            }
        }

        maisExemplarButton.setOnClickListener {
            var qtd = quantidadeEditText.text.toString().toIntOrNull() ?: 1
            qtd++
            quantidadeEditText.setText(qtd.toString())
        }

        confirmarButton.setOnClickListener {
            val qtd = quantidadeEditText.text.toString().toIntOrNull() ?: 1
            if (bookId != null) {
                criarExemplares(bookId, qtd)
            } else {
                Toast.makeText(context, "Erro: ID do livro não encontrado", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        cancelarButton.setOnClickListener {
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }

    private fun criarExemplares(bookId: String, quantidade: Int) {
        // Bloqueia UI para evitar cliques múltiplos e cancelamento acidental
        isCancelable = false
        confirmarButton.isEnabled = false
        cancelarButton.isEnabled = false
        confirmarButton.text = "Criando..."

        lifecycleScope.launch {
            try {
                repeat(quantidade) {
                    val novoExemplar = ExemplarData(
                        bookId = bookId,
                        status = "DISPONIVEL",
                        condition = "BOA"
                    )
                    livroAPI.createBookCopy(novoExemplar)
                }
                
                Toast.makeText(context, "$quantidade exemplares adicionados!", Toast.LENGTH_SHORT).show()
                dismiss()
                
            } catch (e: Exception) {
                Log.e("DIALOG_ADD_EXEMPLARES", "Erro ao criar exemplares: ${e.message}")
                Toast.makeText(context, "Erro ao criar exemplares. Tente novamente.", Toast.LENGTH_SHORT).show()
                
                // Reabilita UI em caso de erro
                isCancelable = true
                confirmarButton.isEnabled = true
                cancelarButton.isEnabled = true
                confirmarButton.text = "Confirmar"
            }
        }
    }
}
