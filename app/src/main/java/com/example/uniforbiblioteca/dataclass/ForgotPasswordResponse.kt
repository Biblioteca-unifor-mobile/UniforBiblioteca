package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordResponse(
    val message: String
)
