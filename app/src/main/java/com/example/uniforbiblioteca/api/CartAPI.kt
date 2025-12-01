package com.example.uniforbiblioteca.api

import com.example.uniforbiblioteca.dataclass.CartCheckoutRequest
import com.example.uniforbiblioteca.dataclass.CartCheckoutResponse
import com.example.uniforbiblioteca.dataclass.CartItem
import com.example.uniforbiblioteca.dataclass.CartItemResponse
import com.example.uniforbiblioteca.dataclass.CartReservationResponse
import com.example.uniforbiblioteca.dataclass.CopyDetailsResponse
import com.example.uniforbiblioteca.dataclass.Loan
import com.example.uniforbiblioteca.dataclass.ReservationResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CartAPI {

    // --- CART ---

    @POST("cart/add/{bookId}")
    suspend fun addToCart(
        @Path("bookId") bookId: String
    ): CartItemResponse

    @GET("cart")
    suspend fun getCart(): List<CartItem>

    @DELETE("cart/{bookId}")
    suspend fun removeFromCart(
        @Path("bookId") bookId: String
    ): CartItemResponse

    @DELETE("cart")
    suspend fun clearCart(): CartItemResponse

    @POST("cart/checkout")
    suspend fun checkout(
        @Body request: CartCheckoutRequest
    ): CartCheckoutResponse


    // --- LOANS (Nova Base URL: /loan) ---

    @GET("loan")
    suspend fun getMyLoans(): List<Loan>

    @GET("loan/{id}")
    suspend fun getLoanDetails(
        @Path("id") loanId: String
    ): Loan

    @GET("loan/admin/copy/{copyId}")
    suspend fun getLoansByCopyId(
        @Path("copyId") copyId: String
    ): CopyDetailsResponse

    @PATCH("loan/{id}/return")
    suspend fun returnBook(
        @Path("id") loanId: String
    ): Any

    @PATCH("loan/{id}/renew")
    suspend fun renewLoan(
        @Path("id") loanId: String
    ): Any


    // --- RESERVATIONS (Nova Base URL: /reservations) ---

    @POST("reservations/{bookId}")
    suspend fun reserveBook(
        @Path("bookId") bookId: String,
        @Body request: CartCheckoutRequest // Usa o mesmo body com dataLimite
    ): CartReservationResponse

    @GET("reservations")
    suspend fun getReservations(): ReservationResponse // Verificar se retorna lista direta ou objeto

    @DELETE("reservations/{id}")
    suspend fun cancelReservation(
        @Path("id") reservationId: String
    ): Any
}
