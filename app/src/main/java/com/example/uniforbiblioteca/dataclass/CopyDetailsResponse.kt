package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CopyDetailsResponse(
    @SerialName("exemplar") val exemplar: ExemplarData? = null,
    @SerialName("livro") val livro: LivroData? = null,
    @SerialName("emprestimoAtual") val emprestimoAtual: LoanDetailsWrapper? = null
)
