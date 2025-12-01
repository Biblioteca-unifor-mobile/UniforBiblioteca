package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Reservation(
    @SerialName("id") val id: String? = null,
    @SerialName("userMatricula") val userMatricula: String? = null,
    @SerialName("bookCopyId") val bookCopyId: String? = null,
    @SerialName("dataReserva") val dataReserva: String? = null,
    @SerialName("dataLimite") val dataLimite: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("bookCopy") val bookCopy: BookCopyWithBook? = null
)
