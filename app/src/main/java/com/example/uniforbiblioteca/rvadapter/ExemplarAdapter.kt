package com.example.uniforbiblioteca.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.Exemplar

class ExemplarAdapter(
    private val exemplares: List<Exemplar>,
    private val onItemClick: (Exemplar) -> Unit
) : RecyclerView.Adapter<ExemplarAdapter.ExemplarViewHolder>() {

    override fun getItemCount() = exemplares.size

    inner class ExemplarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val numero: TextView = itemView.findViewById(R.id.numero_exemplar)
        private val quantidade: TextView = itemView.findViewById(R.id.quantidade_exemplar)
        private val capa: ImageView = itemView.findViewById(R.id.capa_exemplar)
        private val status: TextView = itemView.findViewById(R.id.status_exemplar)
        fun bind(exemplar: Exemplar) {
            numero.text = exemplar.num.toString()
            status.text = exemplar.status
            quantidade.text = itemCount.toString()

            Glide.with(itemView.context)
                .load(exemplar.capa)
                .into(capa)

            itemView.setOnClickListener { onItemClick(exemplar) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExemplarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_exemplares, parent, false)
        return ExemplarViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExemplarViewHolder, position: Int) {
        holder.bind(exemplares[position])
    }

}