package com.example.uniforbiblioteca.rvadapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.dataclass.ChatMassage
import com.example.uniforbiblioteca.R

class ChatAdapter(
    private val messages: List<ChatMassage>
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageView: TextView = itemView.findViewById(R.id.message_chat)
        private val timeView: TextView = itemView.findViewById(R.id.message_chat_time)
        fun bind(message: ChatMassage) {
            messageView.text = message.message
            timeView.text = message.time

            if (message.fromUser){
                val messageParams = messageView.layoutParams as LinearLayout.LayoutParams
                messageParams.gravity = Gravity.END
                messageView.setBackgroundResource(R.drawable.fundo_mensagem_enviada)
                messageView.layoutParams = messageParams
                val timeParams = timeView.layoutParams as LinearLayout.LayoutParams
                timeParams.gravity = Gravity.END
                timeView.layoutParams = timeParams
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_mensagem, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size
}