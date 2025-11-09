package com.example.uniforbiblioteca.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.LivroCardData

class LivrosConfirmacaoAdapter(
    private val books: List<LivroCardData>,
) : RecyclerView.Adapter<LivrosConfirmacaoAdapter.LivroViewHolder>() {

    inner class LivroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.card_confirmacao_titulo)
        private val status: TextView = itemView.findViewById(R.id.card_confirmacao_status)

        fun bind(livro: LivroCardData) {
            titleView.text = livro.titulo
            if (livro.status == "Indisponivel"){
                status.text = livro.status
            } else {
                status.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_confirmacao_livros, parent, false)
        return LivroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size
}