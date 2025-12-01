package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLoans(
    @SerialName("data") val data: MutableList<Loan>
)

