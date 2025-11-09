package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val access_token: String
)

