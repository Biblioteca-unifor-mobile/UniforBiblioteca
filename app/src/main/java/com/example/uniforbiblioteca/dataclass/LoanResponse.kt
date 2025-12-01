package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoanResponse(
    @SerialName("message") val message: String? = null,
    @SerialName("loans") val loans: List<Loan>? = null,
    @SerialName("total") val total: Int? = null
)

