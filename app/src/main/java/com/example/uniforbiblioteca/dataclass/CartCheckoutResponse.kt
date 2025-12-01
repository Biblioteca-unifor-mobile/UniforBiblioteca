package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartCheckoutResponse(
    @SerialName("message") val message: String? = null,
    @SerialName("loans") val loans: List<Loan>? = null,
    @SerialName("cartCleared") val cartCleared: Boolean? = null
)
