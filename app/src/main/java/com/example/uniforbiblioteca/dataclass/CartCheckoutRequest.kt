package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CartCheckoutRequest(
    @SerialName("dataLimite") val dataLimite: String? = null
)