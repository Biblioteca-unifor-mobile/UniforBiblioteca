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

class EmprestadoAdapter(
    private var books: List<LivroCardData>,
    private val onItemClick: (LivroCardData) -> Unit
) : RecyclerView.Adapter<EmprestadoAdapter.EmprestadoViewHolder>() {

    inner class EmprestadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.titulo_card_emprestado)
        private val coverView: ImageView = itemView.findViewById(R.id.imagem_card_emprestado)
        private val tempoView: TextView = itemView.findViewById(R.id.finaliza_card_emprestado)

        fun bind(livro: LivroCardData) {
            titleView.text = livro.titulo
            tempoView.text = livro.tempo;

            Glide.with(itemView.context)
                .load(livro.image)
                .into(coverView)

            itemView.setOnClickListener { onItemClick(livro) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmprestadoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_livroemprestado, parent, false)
        return EmprestadoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmprestadoViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size

    fun updateData(newBooks: List<LivroCardData>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
