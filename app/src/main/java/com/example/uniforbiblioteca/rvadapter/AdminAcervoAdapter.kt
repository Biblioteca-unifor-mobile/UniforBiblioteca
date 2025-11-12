package com.example.uniforbiblioteca.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.LivroData

class AdminAcervoAdapter(
    private var books: List<LivroData>,
    private val onItemClick: (LivroData) -> Unit,
    private val onDeleteClicked: (LivroData) -> Unit,
    private val onEditClicked: (LivroData) -> Unit
) : RecyclerView.Adapter<AdminAcervoAdapter.AdminAcervoViewHolder>() {

    inner class AdminAcervoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.titulo_admin_acervo)
        private val autorView: TextView = itemView.findViewById(R.id.autor_admin_acervo)
        private val coverView: ImageView = itemView.findViewById(R.id.imagem_admin_acervo)

        private val delBtn: Button = itemView.findViewById(R.id.del_acervo_card)
        private val editBtn: Button = itemView.findViewById(R.id.edit_cervo_card)
        fun bind(livro: LivroData) {
            titleView.text = livro.titulo
            autorView.text = livro.autor

            if (livro.imageUrl.toBoolean()) {
                Glide.with(itemView.context)
                    .load(livro.imageUrl)
                    .into(coverView)
            } else {
                coverView.setImageResource(R.drawable.book_cover_placeholder)
            }

            itemView.setOnClickListener {
                onItemClick(livro)
            }

            delBtn.setOnClickListener {
                onDeleteClicked(livro)
            }
            editBtn.setOnClickListener {
                onEditClicked(livro)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAcervoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_acervo_card, parent, false)
        return AdminAcervoViewHolder(view)
    }


    override fun onBindViewHolder(holder: AdminAcervoViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size

    fun updateData(newList: List<LivroData>) {
        books = newList
        notifyDataSetChanged()
    }
}