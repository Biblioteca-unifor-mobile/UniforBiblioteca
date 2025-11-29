package com.example.uniforbiblioteca.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.uniforbiblioteca.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DialogConfirmarDeletarUser(
    private val onDismissCallback: suspend () -> Unit,
) : DialogFragment() {
    private val scope = CoroutineScope(Dispatchers.Main)

    private lateinit var messageTextView: TextView
    private lateinit var confirmarButton: Button
    private lateinit var cancelarButton: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_confirmar_deletar_user, null)

        // Bind das views
        messageTextView = view.findViewById(R.id.message)
        confirmarButton = view.findViewById(R.id.confirmar_deletar)
        cancelarButton = view.findViewById(R.id.cancelar_deletar)

        // Recupera o argumento (nome do usuário)
        val nomeUsuario = arguments?.getString(ARG_NOME) ?: "usuário"

        // Atualiza o texto da mensagem
        messageTextView.text = "Tem certeza que deseja excluir o usuário $nomeUsuario?"

        // Listeners
        confirmarButton.setOnClickListener {
            scope.launch{
                onDismissCallback()
                dismiss()
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
        private const val ARG_NOME = "arg_nome"

        fun newInstance(nome: String?, onDismissCallback: suspend () -> Unit): DialogConfirmarDeletarUser {
            val dialog = DialogConfirmarDeletarUser(onDismissCallback)
            val args = Bundle()
            args.putString(ARG_NOME, nome)
            dialog.arguments = args
            return dialog
        }
    }
}
