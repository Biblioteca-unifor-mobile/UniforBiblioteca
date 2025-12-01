package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CartReservationResponse(
    @SerialName("message") val message: String? = null,
    @SerialName("reservation") val reservation: Reservation? = null
)


