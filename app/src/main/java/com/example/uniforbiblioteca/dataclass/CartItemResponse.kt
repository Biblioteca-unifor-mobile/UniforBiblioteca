package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItemResponse(
    @SerialName("message") val message: String? = null,
    @SerialName("cartItem") val cartItem: CartItem? = null,
    @SerialName("bookId") val bookId: String? = null,
    @SerialName("itemsRemoved") val itemsRemoved: Int? = null
)
