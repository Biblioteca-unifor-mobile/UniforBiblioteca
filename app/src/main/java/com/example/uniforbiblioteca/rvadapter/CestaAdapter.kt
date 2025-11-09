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

class CestaAdapter(
    private val books: List<LivroCardData>,
    private val onItemClick: (LivroCardData) -> Unit
) : RecyclerView.Adapter<CestaAdapter.CestaViewHolder>() {

    inner class CestaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.titulo_card_cesta)
        private val autorView: TextView = itemView.findViewById(R.id.autor_card_cesta)
        private val coverView: ImageView = itemView.findViewById(R.id.imagem_card_cesta)

        fun bind(livro: LivroCardData) {
            titleView.text = livro.titulo
            autorView.text = livro.autor

            Glide.with(itemView.context)
                .load(livro.image)
                .into(coverView)

            itemView.setOnClickListener { onItemClick(livro) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CestaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_cesta, parent, false)
        return CestaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CestaViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size
}