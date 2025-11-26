package com.example.uniforbiblioteca.api

import com.example.uniforbiblioteca.dataclass.CartCheckoutRequest
import com.example.uniforbiblioteca.dataclass.CartCheckoutResponse
import com.example.uniforbiblioteca.dataclass.CartItemResponse
import com.example.uniforbiblioteca.dataclass.CartReservationResponse
import com.example.uniforbiblioteca.dataclass.CartResponse
import com.example.uniforbiblioteca.dataclass.LoanResponse
import com.example.uniforbiblioteca.dataclass.ReservationResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CartAPI {

    // --- CART ---

    @POST("cart/add/{bookCopyId}")
    suspend fun addToCart(
        @Path("bookCopyId") bookCopyId: String
    ): CartItemResponse

    @GET("cart")
    suspend fun getCart(): CartResponse

    @DELETE("cart/{bookId}")
    suspend fun removeFromCart(
        @Path("bookId") bookId: String
    ): CartItemResponse // ou um objeto de sucesso simples

    @DELETE("cart")
    suspend fun clearCart(): CartItemResponse // ou objeto de sucesso

    @POST("cart/checkout")
    suspend fun checkout(
        @Body request: CartCheckoutRequest
    ): CartCheckoutResponse

    // --- RESERVATIONS ---

    @POST("cart/reserve/{bookId}")
    suspend fun reserveBook(
        @Path("bookId") bookId: String,
        @Body request: CartCheckoutRequest // Usa o mesmo body com dataLimite
    ): CartReservationResponse

    @GET("cart/reservations")
    suspend fun getReservations(): ReservationResponse

    // --- LOANS & RETURNS ---

    @POST("cart/return/{loanId}")
    suspend fun returnBook(
        @Path("loanId") loanId: String
    ): Any // Retorna o loan atualizado

    @GET("cart/loans")
    suspend fun getMyLoans(): LoanResponse
}
