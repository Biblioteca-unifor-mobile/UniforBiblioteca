package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    @SerialName("matricula") val matricula: String? = null,
    @SerialName("nome") val nome: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("senha") val senha: String? = null,
    @SerialName("role") val role: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
)



