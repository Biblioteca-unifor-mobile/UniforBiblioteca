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
import com.example.uniforbiblioteca.dataclass.Loan
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class HistoricoAdapter(
    private val emprestimos: MutableList<Loan>,
    private val onItemClick: (Loan) -> Unit
) : RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder>() {

    inner class HistoricoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.titulo_card_historico)
        private val authorView: TextView = itemView.findViewById(R.id.autor_card_historico)
        private val coverView: ImageView = itemView.findViewById(R.id.imagem_card_historico)
        private val tempoView: TextView = itemView.findViewById(R.id.tempo_card_historico)

        fun bind(emprestimo: Loan) {
            val livro = emprestimo.bookCopy?.book
            titleView.text = livro?.titulo
            authorView.text = livro?.autor
            val dataString = emprestimo.dataEmprestimo
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'")
            val data = LocalDateTime.parse(dataString, formatter)
            val agora = LocalDateTime.now()

            val tempoDias =  ChronoUnit.DAYS.between(data, agora)
            tempoView.text = tempoDias.toString() + " dias atrás";

            if (livro?.imageUrl != null) {
                Glide.with(itemView.context)
                    .load(livro.imageUrl)
                    .into(coverView)
            }

            itemView.setOnClickListener { onItemClick(emprestimo) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_livrohistorico, parent, false)
        return HistoricoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
        holder.bind(emprestimos[position])
    }

    override fun getItemCount() = emprestimos.size

    fun updateItems(items: MutableList<Loan>){
        emprestimos.clear()
        emprestimos.addAll(items)
        notifyDataSetChanged()
    }
}