package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LivroPagination(
    @SerialName("_page") val page: Int? = null,
    @SerialName("_limit") val limit: Int? = null,
    @SerialName("_total") val total: Int? = null,
    @SerialName("_totalPages") val totalPages: Int? = null
)