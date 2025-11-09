package com.example.uniforbiblioteca.api

import com.example.uniforbiblioteca.dataclass.LoginResponse
import com.example.uniforbiblioteca.dataclass.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsuarioAPI {

    @POST("auth/login")
    suspend fun login(
        @Body user: Usuario
    ): LoginResponse


    @POST("auth/register")
    suspend fun register(
        @Body user: Usuario
    ): LoginResponse

    @GET("auth/profile")
    suspend fun profile(): Usuario
}
