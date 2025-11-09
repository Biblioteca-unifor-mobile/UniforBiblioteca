package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.Serializable

@Serializable
enum class Role {
    ALUNO,
    ADMINISTRADOR
}