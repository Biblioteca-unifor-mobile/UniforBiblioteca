package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserList(
    @SerialName("count") val count: Int = 0,
    @SerialName("data") val data: MutableList<Usuario> = mutableListOf(),
)
