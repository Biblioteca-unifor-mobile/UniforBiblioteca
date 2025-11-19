package com.example.uniforbiblioteca.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApi {
    
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): Response<ChatResponse>
}

// Data classes para a API da OpenAI
data class ChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<OpenAIMessage>,
    val max_tokens: Int = 300,
    val temperature: Double = 0.7
)

data class OpenAIMessage(
    val role: String,  // "system", "user" ou "assistant"
    val content: String
)

data class ChatResponse(
    val id: String,
    val choices: List<Choice>,
    val usage: Usage?
)

data class Choice(
    val index: Int,
    val message: OpenAIMessage,
    val finish_reason: String?
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

