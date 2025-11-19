package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CartResponse(
    @SerialName("message") val message: String? = null,
    @SerialName("items") val items: List<CartItem>? = null,
    @SerialName("total") val total: Int? = null
)

@Serializable
data class CartItem(
    @SerialName("userMatricula") val userMatricula: String? = null,
    @SerialName("bookId") val bookId: String? = null,
    @SerialName("addedAt") val addedAt: String? = null,
    @SerialName("book") val book: LivroData? = null
)

@Serializable
data class CartItemResponse(
    @SerialName("message") val message: String? = null,
    @SerialName("cartItem") val cartItem: CartItem? = null,
    @SerialName("bookId") val bookId: String? = null,
    @SerialName("itemsRemoved") val itemsRemoved: Int? = null
)
