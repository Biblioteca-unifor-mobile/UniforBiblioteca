package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.BuildConfig
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.api.ChatRequest
import com.example.uniforbiblioteca.api.OpenAIApi
import com.example.uniforbiblioteca.api.OpenAIClient
import com.example.uniforbiblioteca.api.OpenAIMessage
import com.example.uniforbiblioteca.dataclass.ChatMassage
import com.example.uniforbiblioteca.rvadapter.ChatAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatFragment : Fragment() {

    private lateinit var voltarBtn: Button
    private lateinit var sendBtn: ImageButton
    private lateinit var inputMessage: EditText
    private lateinit var chatRecyclerView: RecyclerView
    
    private val messagesList = mutableListOf<ChatMassage>()
    private lateinit var adapter: ChatAdapter
    private var messageIdCounter = 1
    private val conversationHistory = mutableListOf<OpenAIMessage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        voltarBtn = view.findViewById(R.id.chat_voltar_btn)
        sendBtn = view.findViewById(R.id.chat_send_button)
        inputMessage = view.findViewById(R.id.chat_input_message)
        chatRecyclerView = view.findViewById(R.id.chat_rv)

        // Mensagem de boas-vindas do assistente
        messagesList.add(
            ChatMassage(
                messageIdCounter++,
                "Olá! Sou o assistente virtual da Biblioteca Unifor. Como posso ajudá-lo hoje? Posso responder perguntas sobre livros, empréstimos, reservas e muito mais!",
                getCurrentTime(),
                false
            )
        )
        
        // Inicializa o histórico da conversa com o contexto do sistema
        conversationHistory.add(
            OpenAIMessage(
                role = "system",
                content = "Você é um assistente virtual da Biblioteca da Unifor (Universidade de Fortaleza). " +
                        "Você ajuda estudantes com informações sobre livros, empréstimos, reservas, horários da biblioteca, " +
                        "e dúvidas gerais sobre o acervo. Seja educado, prestativo e objetivo nas respostas. " +
                        "Responda em português brasileiro."
            )
        )

        adapter = ChatAdapter(messagesList)
        chatRecyclerView.adapter = adapter
        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        voltarBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        sendBtn.setOnClickListener {
            sendMessage()
        }
        
        // Também envia ao pressionar Enter
        inputMessage.setOnEditorActionListener { _, _, _ ->
            sendMessage()
            true
        }

        return view
    }
    
    private fun sendMessage() {
        val messageText = inputMessage.text.toString().trim()
        
        if (messageText.isEmpty()) {
            Toast.makeText(requireContext(), "Digite uma mensagem", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Adiciona mensagem do usuário à lista
        val userMessage = ChatMassage(
            messageIdCounter++,
            messageText,
            getCurrentTime(),
            true
        )
        messagesList.add(userMessage)
        adapter.notifyItemInserted(messagesList.size - 1)
        chatRecyclerView.scrollToPosition(messagesList.size - 1)
        
        // Adiciona ao histórico da conversa
        conversationHistory.add(
            OpenAIMessage(role = "user", content = messageText)
        )
        
        // Limpa input
        inputMessage.text.clear()
        
        // Desabilita botão enquanto processa
        sendBtn.isEnabled = false
        
        // Chama a API da OpenAI
        sendToOpenAI()
    }
    
    private fun sendToOpenAI() {
        lifecycleScope.launch {
            try {
                // Cria a API
                val api = OpenAIClient.create(BuildConfig.OPENAI_API_KEY)
                    .create(OpenAIApi::class.java)
                
                // Prepara a requisição com todo o histórico da conversa
                val request = ChatRequest(
                    model = "gpt-3.5-turbo",
                    messages = conversationHistory.toList(),
                    max_tokens = 300,
                    temperature = 0.7
                )
                
                // Faz a chamada
                val response = api.sendMessage(request)
                
                if (response.isSuccessful && response.body() != null) {
                    val botResponseMessage = response.body()!!.choices.firstOrNull()?.message
                    
                    if (botResponseMessage != null) {
                        // Adiciona resposta do bot ao histórico
                        conversationHistory.add(botResponseMessage)
                        
                        // Adiciona resposta do bot à lista de mensagens
                        val botMessage = ChatMassage(
                            messageIdCounter++,
                            botResponseMessage.content,
                            getCurrentTime(),
                            false
                        )
                        messagesList.add(botMessage)
                        adapter.notifyItemInserted(messagesList.size - 1)
                        chatRecyclerView.scrollToPosition(messagesList.size - 1)
                    } else {
                        showError("Resposta vazia do assistente")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Erro desconhecido"
                    showError("Erro ao se comunicar: ${response.code()}")
                    android.util.Log.e("ChatFragment", "Erro API: $errorMessage")
                }
                
            } catch (e: Exception) {
                showError("Erro: ${e.message}")
                android.util.Log.e("ChatFragment", "Exceção ao chamar OpenAI", e)
            } finally {
                // Reabilita botão
                sendBtn.isEnabled = true
            }
        }
    }
    
    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
    
    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
    
    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("Chat")
    }
}
