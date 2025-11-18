package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val newPassword: String
)
