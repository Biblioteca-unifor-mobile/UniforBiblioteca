package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserFolders(
    @SerialName("data") val data: MutableList<Folder>
)
