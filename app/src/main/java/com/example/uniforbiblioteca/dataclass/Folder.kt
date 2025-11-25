package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable


@Serializable
data class FolderUser(
    @SerialName("folderId") val folderId: String? = null,
    @SerialName("userMatricula") val matricula: String? = null,
    @SerialName("role") val role: String? = null,
)

@Serializable
data class FolderBook(
    @SerialName("bookId") val bookId: String? = null,
)

@Serializable
data class Folder(
    @SerialName("id") val id: String? = null,
    @SerialName("nome") var nome: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null,
    @SerialName("users") val users: MutableList<FolderUser>? = null,
    @SerialName("books") val books: MutableList<LivroData>? = null
): JavaSerializable