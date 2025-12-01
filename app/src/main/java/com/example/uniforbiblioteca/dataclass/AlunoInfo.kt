package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlunoInfo(
    @SerialName("matricula") val matricula: String? = null,
    @SerialName("nome") val nome: String? = null,
    @SerialName("email") val email: String? = null
)
