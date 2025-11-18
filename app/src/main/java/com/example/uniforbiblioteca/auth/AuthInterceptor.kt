package com.example.uniforbiblioteca.auth

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(
    private val context: Context?
) : Interceptor {

    private val rotasPublicas = listOf(
        "/auth/login",
        "/auth/register",
        "/auth/forgot-password",
        "/auth/verify-code"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val urlPath = request.url.encodedPath

        val isPublic = rotasPublicas.any { urlPath.endsWith(it) }

        if (isPublic) {
            return chain.proceed(request)
        }

        val tokenHandler = AuthTokenHandler(context)
        val token = tokenHandler.getToken()

        if (token.isNullOrEmpty()) {
            throw IOException("Token ausente — rota protegida requer autenticação.")
        }

        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}
