package com.example.uniforbiblioteca.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.dataclass.PastaCardData
import com.example.uniforbiblioteca.R

class PastaAdapter(
    private val pastas: List<PastaCardData>,
    private val onItemClick: (PastaCardData) -> Unit
) : RecyclerView.Adapter<PastaAdapter.PastaViewHolder>() {

    inner class PastaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.titulo_card_pasta)
        private val tempoView: TextView = itemView.findViewById(R.id.card_ultima_modificacao)

        fun bind(pasta: PastaCardData) {
            titleView.text = pasta.titulo
            tempoView.text = pasta.lastModified;


            itemView.setOnClickListener { onItemClick(pasta) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pasta, parent, false)
        return PastaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PastaViewHolder, position: Int) {
        holder.bind(pastas[position])
    }

    override fun getItemCount() = pastas.size
}
