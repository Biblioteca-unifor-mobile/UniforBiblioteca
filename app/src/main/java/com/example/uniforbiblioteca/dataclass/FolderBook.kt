package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FolderBook(
    @SerialName("bookId") val bookId: String? = null,
)
