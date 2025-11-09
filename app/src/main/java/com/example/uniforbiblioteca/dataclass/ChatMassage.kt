package com.example.uniforbiblioteca.dataclass

data class ChatMassage(
    val id: Int,
    val message: String,
    val time: String,
    val fromUser: Boolean
)