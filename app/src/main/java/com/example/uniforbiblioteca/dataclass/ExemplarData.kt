package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

@Serializable
data class ExemplarData(
    @SerialName("_id") val id: String? = null,
    @SerialName("_bookId") val bookId: String? = null,
    @SerialName("_copyNumber") val copyNumber: Int? = null,
    @SerialName("_status") val status: String? = null,
    @SerialName("_condition") var condition: String? = null,
    @SerialName("_book") val book: LivroData? = null
)
