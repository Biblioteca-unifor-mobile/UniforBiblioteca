package com.example.uniforbiblioteca.dataclass

data class Exemplar(
    val id: Int,
    val livroId: Int,
    val num: Int,
    val status: String,
    val capa: String
)