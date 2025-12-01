package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FolderUser(
    @SerialName("folderId") val folderId: String? = null,
    @SerialName("userMatricula") val matricula: String? = null,
    @SerialName("role") val role: String? = null,
)
