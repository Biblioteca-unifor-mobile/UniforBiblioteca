package com.example.uniforbiblioteca.dataclass

data class LivroCardData(val id: String, val titulo: String, val autor: String, val tempo: String, val image: String, val status: String = "Disponivel")
