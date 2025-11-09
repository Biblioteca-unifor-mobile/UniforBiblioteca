package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class LivroResponse(
    val data: List<LivroData>? = null,
    val pagination: LivroPagination? = null
)