package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Loan(
    @SerialName("id") val id: String? = null,
    @SerialName("userMatricula") val userMatricula: String? = null,
    @SerialName("bookCopyId") val bookCopyId: String? = null,
    @SerialName("dataEmprestimo") val dataEmprestimo: String? = null,
    @SerialName("dataLimite") val dataLimite: String? = null,
    @SerialName("dataDevolucao") val dataDevolucao: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("renovacoes") val renovacoes: Int? = null,
    @SerialName("divida") val divida: Double? = null,
    @SerialName("bookCopy") val bookCopy: BookCopyWithBook? = null,
    @SerialName("emprestimoAtual") val emprestimoAtual: LoanDetailsWrapper? = null
)

