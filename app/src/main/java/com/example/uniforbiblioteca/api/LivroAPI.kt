package com.example.uniforbiblioteca.api

import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dataclass.LivroResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface LivroAPI {
    @GET("books")
    suspend fun getBooks(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): LivroResponse

    @POST("books")
    suspend fun createBook(
        @Body livro: LivroData
    ): Unit

    @PATCH("books/{id}  ")
    suspend fun pacthBook(
        @Path("id") id: String,
        @Body livro: LivroData
    ): LivroData

    @GET("books/{id}")
    suspend fun getBook(
        @Path("id") id: String,
    ): LivroData

    @DELETE("books/{id}")
    suspend fun deleteBook(
        @Path("id") id: String
    ): Unit
}