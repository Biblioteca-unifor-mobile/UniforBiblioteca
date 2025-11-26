package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CartCheckoutRequest(
    @SerialName("dataLimite") val dataLimite: String? = null
)

@Serializable
data class CartCheckoutResponse(
    @SerialName("message") val message: String? = null,
    @SerialName("loans") val loans: List<Loan>? = null,
    @SerialName("cartCleared") val cartCleared: Boolean? = null
)

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
    @SerialName("bookCopy") val bookCopy: BookCopyWithBook? = null
)

@Serializable
data class BookCopyWithBook(
    @SerialName("id") val id: String? = null,
    @SerialName("copyNumber") val copyNumber: Int? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("condition") val condition: String? = null,
    @SerialName("book") val book: LivroData? = null
)

@Serializable
data class LoanResponse(
    @SerialName("message") val message: String? = null,
    @SerialName("loans") val loans: List<Loan>? = null,
    @SerialName("total") val total: Int? = null
)
