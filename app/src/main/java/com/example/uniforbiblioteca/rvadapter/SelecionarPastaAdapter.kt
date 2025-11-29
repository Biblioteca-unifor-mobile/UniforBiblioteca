package com.example.uniforbiblioteca.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.dataclass.Folder
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.fragment.PastaFragment
import com.example.uniforbiblioteca.viewmodel.FolderManager
import java.text.SimpleDateFormat
import java.util.Locale

class SelecionarPastaAdapter(
    private val pastas: MutableList<Folder>,
    private val onItemClick: (Folder) -> Unit
) : RecyclerView.Adapter<SelecionarPastaAdapter.PastaViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class PastaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.titulo_card_pasta)
        private val tempoView: TextView = itemView.findViewById(R.id.card_ultima_modificacao)
        private val frame: ConstraintLayout = itemView.findViewById(R.id.frame_card)

        fun bind(pasta: Folder, isSelected: Boolean) {
            titleView.text = pasta.nome
            tempoView.text = formatarData(pasta.updatedAt)

            val backgroundRes = if (isSelected)
                R.drawable.frame_card_selected
            else
                R.drawable.frame_card_default

            frame.setBackgroundResource(backgroundRes)

            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition

                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onItemClick(pasta)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pasta, parent, false)
        return PastaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PastaViewHolder, position: Int) {
        holder.bind(pastas[position], position == selectedPosition)
    }

    override fun getItemCount() = pastas.size

    private fun formatarData(dataStr: String?): String {
        if (dataStr == null) return ""
        try {
            // Formato que vem da API: "2025-12-05T23:59:59Z"
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'", Locale.US)
            val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR"))
            val date = inputFormat.parse(dataStr)
            return if (date != null) outputFormat.format(date) else dataStr
        } catch (e: Exception) {
            return dataStr
        }
    }
}
