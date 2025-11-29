package com.example.uniforbiblioteca.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.fragment.PastaFragment
import com.example.uniforbiblioteca.viewmodel.FolderManager
import kotlinx.coroutines.launch

class DialogDeletarLivroPasta(
    private val onDismissCallback: () -> Unit,
    ) : DialogFragment() {

    private lateinit var messageTextView: TextView
    private lateinit var confirmarButton: Button
    private lateinit var cancelarButton: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_confirmar_deletar_user, null)

        messageTextView = view.findViewById(R.id.message)
        confirmarButton = view.findViewById(R.id.confirmar_deletar)
        cancelarButton = view.findViewById(R.id.cancelar_deletar)

        // Recupera o argumento (objeto Livro)
        val livro: LivroData? = arguments?.getSerializable(ARG_LIVRO) as? LivroData

        messageTextView.text = "Tem certeza que deseja remover o livro ${livro?.titulo ?: ""}?"

        confirmarButton.setOnClickListener {
            lifecycleScope.launch {

                try {
                    FolderManager.deleteBookFromFolder(livro?.id!!)
                    Toast.makeText(context, "livro removido com sucesso", Toast.LENGTH_LONG).show()
                    onDismissCallback()
                    dismiss()
                } catch (e: Exception){
                    Toast.makeText(context, "Erro ao remover livro", Toast.LENGTH_LONG).show()
                }
            }
        }

        cancelarButton.setOnClickListener {
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    companion object {
        private const val ARG_LIVRO = "arg_livro"

        fun newInstance(livro: LivroData, onDismissCallback: () -> Unit): DialogDeletarLivroPasta {
            val dialog = DialogDeletarLivroPasta(onDismissCallback)
            val args = Bundle()
            args.putSerializable(ARG_LIVRO, livro)
            dialog.arguments = args
            return dialog
        }
    }
}

