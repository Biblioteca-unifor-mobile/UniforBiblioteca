package com.example.uniforbiblioteca.api

import com.example.uniforbiblioteca.dataclass.ExemplarData
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dataclass.LivroResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LivroAPI {
    // --- BOOKS ---

    @GET("books")
    suspend fun getBooks(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("titulo") titulo: String? = null,
        @Query("autor") autor: String? = null,
        @Query("isbn") isbn: String? = null,
        @Query("anoEdicao") anoEdicao: Int? = null,
        @Query("edicao") edicao: String? = null,
        @Query("search") search: String? = null
    ): LivroResponse

    @POST("books")
    suspend fun createBook(
        @Body livro: LivroData
    ): LivroData

    @PATCH("books/{id}")
    suspend fun pacthBook(
        @Path("id") id: String?,
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

    // --- BOOK COPIES ---

    @POST("book-copies")
    suspend fun createBookCopy(
        @Body exemplar: ExemplarData
    ): ExemplarData

    @GET("book-copies")
    suspend fun getBookCopies(): List<ExemplarData>

    @GET("book-copies/book/{bookId}")
    suspend fun getBookCopiesByBook(
        @Path("bookId") bookId: String
    ): List<ExemplarData>

    @GET("book-copies/{id}")
    suspend fun getBookCopy(
        @Path("id") id: String
    ): ExemplarData

    @PATCH("book-copies/{id}")
    suspend fun patchBookCopy(
        @Path("id") id: String,
        @Body exemplar: ExemplarData
    ): ExemplarData

    @DELETE("book-copies/{id}")
    suspend fun deleteBookCopy(
        @Path("id") id: String
    ): Unit
}
