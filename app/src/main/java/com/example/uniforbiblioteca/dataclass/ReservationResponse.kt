package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReservationResponse(
    @SerialName("message") val message: String? = null,
    @SerialName("reservations") val reservations: List<Reservation>? = null,
    @SerialName("total") val total: Int? = null
)

