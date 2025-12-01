package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserReservations(
    @SerialName("data") val data: MutableList<Reservation>
)
