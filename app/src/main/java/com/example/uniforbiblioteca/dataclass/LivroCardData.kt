package com.example.uniforbiblioteca.dataclass

data class LivroCardData(val id: Int, val titulo: String, val autor: String, val tempo: String, val image: String, val status: String = "Disponivel")
