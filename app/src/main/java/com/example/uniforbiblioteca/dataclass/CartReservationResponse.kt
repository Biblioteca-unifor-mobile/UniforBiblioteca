package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CartReservationResponse(
    @SerialName("message") val message: String? = null,
    @SerialName("reservation") val reservation: Reservation? = null
)

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

@Serializable
data class ReservationResponse(
    @SerialName("message") val message: String? = null,
    @SerialName("reservations") val reservations: List<Reservation>? = null,
    @SerialName("total") val total: Int? = null
)
