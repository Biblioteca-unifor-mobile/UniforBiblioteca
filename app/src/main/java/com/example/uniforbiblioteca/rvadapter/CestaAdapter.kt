package com.example.uniforbiblioteca.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uniforbiblioteca.dataclass.CartItem
import com.example.uniforbiblioteca.R

class CestaAdapter(
    private var items: List<CartItem>,
    private val onDeleteClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CestaAdapter.CestaViewHolder>() {

    inner class CestaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.titulo_card_cesta)
        private val autorView: TextView = itemView.findViewById(R.id.autor_card_cesta)
        private val coverView: ImageView = itemView.findViewById(R.id.imagem_card_cesta)
        private val deleteBtn: Button = itemView.findViewById(R.id.deletCestaBtn)

        fun bind(item: CartItem) {
            val livro = item.book
            titleView.text = livro?.titulo ?: "Sem título"
            autorView.text = livro?.autor ?: "Sem autor"

            if (!livro?.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(livro.imageUrl)
                    .into(coverView)
            } else {
                coverView.setImageResource(R.drawable.book_cover_placeholder)
            }

            // Listener no botão X
            deleteBtn.setOnClickListener {
                onDeleteClick(item)
            }

            // Opcional: Manter o long click como alternativa
            itemView.setOnLongClickListener {
                onDeleteClick(item)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CestaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_cesta, parent, false)
        return CestaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CestaViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
