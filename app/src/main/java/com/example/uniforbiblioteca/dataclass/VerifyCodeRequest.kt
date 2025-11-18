package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class VerifyCodeRequest(
    val email: String,
    val code: String
)
