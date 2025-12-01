package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookCopyWithBook(
    @SerialName("id") val id: String? = null,
    @SerialName("copyNumber") val copyNumber: Int? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("condition") val condition: String? = null,
    @SerialName("book") val book: LoanBook? = null
)



