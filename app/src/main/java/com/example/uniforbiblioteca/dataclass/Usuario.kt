package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    @SerialName("_matricula") val matricula: String? = null,
    @SerialName("_nome") val nome: String? = null,
    @SerialName("_email") val email: String? = null,
    @SerialName("_senha") val senha: String? = null,
    @SerialName("_role") val role: String? = null,
    @SerialName("_createdAt") val createdAt: String? = null,
    @SerialName("_updatedAt") val updatedAt: String? = null
)


