package com.example.uniforbiblioteca.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.dataclass.MembroPasta
import com.example.uniforbiblioteca.R

class MembroAdapter(private val membros: List<MembroPasta>) :
    RecyclerView.Adapter<MembroAdapter.MembroViewHolder>() {

    class MembroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.membro_nome)
        val spinner: Spinner = itemView.findViewById(R.id.membro_options)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_membro_pasta, parent, false)
        return MembroViewHolder(view)
    }

    override fun onBindViewHolder(holder: MembroViewHolder, position: Int) {
        val user = membros[position]
        holder.nomeTextView.text = user.nome

        val context = holder.itemView.context
        val options = listOf("Proprietário", "Editor", "Leitor")
        val spinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, options)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinner.adapter = spinnerAdapter

        holder.spinner.setSelection(user.role)
    }

    override fun getItemCount(): Int = membros.size
}