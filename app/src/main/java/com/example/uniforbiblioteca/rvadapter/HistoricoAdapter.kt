package com.example.uniforbiblioteca.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.R

class HistoricoAdapter(
    private val books: List<LivroCardData>,
    private val onItemClick: (LivroCardData) -> Unit
) : RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder>() {

    inner class HistoricoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.titulo_card_historico)
        private val authorView: TextView = itemView.findViewById(R.id.autor_card_historico)
        private val coverView: ImageView = itemView.findViewById(R.id.imagem_card_historico)
        private val tempoView: TextView = itemView.findViewById(R.id.tempo_card_historico)

        fun bind(livro: LivroCardData) {
            titleView.text = livro.titulo
            authorView.text = livro.autor
            tempoView.text = livro.tempo;

            Glide.with(itemView.context)
                .load(livro.image)
                .into(coverView)

            itemView.setOnClickListener { onItemClick(livro) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_livrohistorico, parent, false)
        return HistoricoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size
}