package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoanBook(
    @SerialName("id") val id: String? = null,
    @SerialName("titulo") val titulo: String? = null,
    @SerialName("autor") val autor: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null
)

