package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable




@Serializable
data class Folder(
    @SerialName("id") val id: String? = null,
    @SerialName("nome") var nome: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null,
    @SerialName("users") val users: MutableList<FolderUser>? = null,
    @SerialName("books") val books: MutableList<LivroData>? = null
): JavaSerializable
