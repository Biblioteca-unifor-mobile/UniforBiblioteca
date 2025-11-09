package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.ChatMassage
import com.example.uniforbiblioteca.rvadapter.ChatAdapter

class ChatFragment : Fragment() {

    private lateinit var voltarBtn: Button
    private lateinit var sendBtn: ImageButton
    private lateinit var inputMessage: EditText
    private lateinit var chatRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        voltarBtn = view.findViewById(R.id.chat_voltar_btn)
        sendBtn = view.findViewById(R.id.chat_send_button)
        inputMessage = view.findViewById(R.id.chat_input_message)
        chatRecyclerView = view.findViewById(R.id.chat_rv)

        val messages = listOf(
            ChatMassage(1, "Oi! Como você está?", "10:00", true),
            ChatMassage(2, "Tudo bem, e você?", "10:01", false),
            ChatMassage(3, "Estou começando a ler um livro novo.", "10:02", true),
            ChatMassage(4, "Que legal! Qual livro?", "10:03", false),
            ChatMassage(5, "É sobre ciência de dados em Python.", "10:04", true)
        )

        val adapter = ChatAdapter(messages)
        chatRecyclerView.adapter = adapter
        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        voltarBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        sendBtn.setOnClickListener {
            // TODO: lógica de envio de mensagem
        }

        return view
    }
    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("Chat")
    }
}
