package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    @SerialName("userMatricula") val userMatricula: String? = null,
    @SerialName("bookId") val bookId: String? = null,
    @SerialName("addedAt") val addedAt: String? = null,
    @SerialName("book") val book: LivroData? = null
)
