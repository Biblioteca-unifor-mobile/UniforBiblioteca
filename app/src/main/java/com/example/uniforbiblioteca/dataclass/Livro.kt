package com.example.uniforbiblioteca.dataclass

data class Livro(
    val id: Int,
    val capa: String,
    val tipo: String,
    val autor: String,
    val coautor1: String,
    val coautor2: String,
    val edicao: String,
    val anoEdicao: String,
    val idioma: String,
    val publicacao: String,
    val resumo: String,
    val isbn: String,
    val qtdRestante: Int,
    val qtdTotal: Int
)
