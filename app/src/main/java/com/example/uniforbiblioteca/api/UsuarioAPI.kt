package com.example.uniforbiblioteca.api

import com.example.uniforbiblioteca.dataclass.ForgotPasswordRequest
import com.example.uniforbiblioteca.dataclass.LoginResponse
import com.example.uniforbiblioteca.dataclass.ForgotPasswordResponse
import com.example.uniforbiblioteca.dataclass.ResetPasswordRequest
import com.example.uniforbiblioteca.dataclass.ResetPasswordResponse
import com.example.uniforbiblioteca.dataclass.Usuario
import com.example.uniforbiblioteca.dataclass.VerifyCodeRequest
import com.example.uniforbiblioteca.dataclass.VerifyCodeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsuarioAPI {

    @POST("auth/login")
    suspend fun login(
        @Body user: Usuario
    ): LoginResponse

    @POST("/auth/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): ForgotPasswordResponse


    @POST("/auth/verify-code")
    suspend fun verifyCode(@Body body: VerifyCodeRequest): VerifyCodeResponse

    @POST("/auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): ResetPasswordResponse

    @POST("auth/register")
    suspend fun register(
        @Body user: Usuario
    ): LoginResponse

    @GET("auth/profile")
    suspend fun profile(): Usuario
}
