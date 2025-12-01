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
import com.example.uniforbiblioteca.dataclass.LivroData

final class AcervoAdapter(
    private var books: MutableList<LivroData>,
    private val onItemClick: (LivroData) -> Unit
) : RecyclerView.Adapter<AcervoAdapter.AcervoViewHolder>() {

    inner class AcervoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.titulo_card_acervo)
        private val autorView: TextView = itemView.findViewById(R.id.autor_card_acervo)
        private val coverView: ImageView = itemView.findViewById(R.id.imagem_card_acervo)
        fun bind(livro: LivroData) {
            titleView.text = livro.titulo
            autorView.text = livro.autor

            // Correção: Verificar se a URL não é nula ou vazia
            if (!livro.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(livro.imageUrl)
                    .placeholder(R.drawable.book_cover_placeholder) // Opcional: mostra enquanto carrega
                    .error(R.drawable.book_cover_placeholder) // Opcional: mostra se falhar
                    .into(coverView)
            } else {
                coverView.setImageResource(R.drawable.book_cover_placeholder)
            }

            itemView.setOnClickListener {
                onItemClick(livro)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcervoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_livroacervo, parent, false)
        return AcervoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AcervoViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size


    fun updateData(newList: MutableList<LivroData>) {
        books.clear()
        books.addAll(newList)
        notifyDataSetChanged()
    }
}
