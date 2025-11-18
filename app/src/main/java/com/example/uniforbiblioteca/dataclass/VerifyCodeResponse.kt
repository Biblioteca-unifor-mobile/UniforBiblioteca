package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class VerifyCodeResponse(
    val access_token: String?
)
