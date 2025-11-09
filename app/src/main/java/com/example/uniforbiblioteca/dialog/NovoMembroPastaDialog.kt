package com.example.uniforbiblioteca.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.MembroPasta
import com.example.uniforbiblioteca.rvadapter.MembroAdapter

class NovoMembroPastaDialog : DialogFragment() {

    private lateinit var tituloTextView: TextView
    private lateinit var inputNovoMembro: EditText
    private lateinit var addMembroButton: Button
    private lateinit var cancelarMembroButton: Button
    private lateinit var membroRecyclerView: RecyclerView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.dialog_membros_pasta, null)

        // Inicializa views
        tituloTextView = view.findViewById(R.id.textView48)
        inputNovoMembro = view.findViewById(R.id.input_novo_membro)
        addMembroButton = view.findViewById(R.id.add_membro_button)
        cancelarMembroButton = view.findViewById(R.id.cancelar_membro_button)
        membroRecyclerView = view.findViewById(R.id.membro_rv)

        val membros = listOf(
            MembroPasta(1, "João Victor", 0),
            MembroPasta(2, "Maria Clara", 2),
            MembroPasta(3, "Pedro Henrique", 1),
            MembroPasta(4, "Ana Beatriz", 2),
            MembroPasta(5, "Lucas Silva", 2),
            MembroPasta(6, "Carla Souza", 1),
            MembroPasta(7, "Rafael Costa", 2),
            MembroPasta(8, "Fernanda Lima", 2),
            MembroPasta(9, "Gustavo Martins", 1),
            MembroPasta(10, "Isabela Rocha", 2)
        )


        val adapter = MembroAdapter(membros)
        membroRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        membroRecyclerView.adapter = adapter


        // Listeners vazios
        addMembroButton.setOnClickListener {
            // TODO: ação de adicionar membro
        }

        cancelarMembroButton.setOnClickListener {
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}