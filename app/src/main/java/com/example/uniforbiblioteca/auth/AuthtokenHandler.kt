package com.example.uniforbiblioteca.auth

import android.content.Context
import android.util.Base64
import androidx.core.content.edit
import org.json.JSONObject

class AuthTokenHandler(private val context: Context?) {

    private val sharedPref = context?.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val TOKEN_KEY = "auth_token"

    fun getToken(): String? {
        return sharedPref?.getString(TOKEN_KEY, null)
    }

    fun setToken(token: String) {
        sharedPref?.edit {
            putString(TOKEN_KEY, token)
        }
    }

    fun clearToken() {
        sharedPref?.edit {
            remove(TOKEN_KEY)
        }
    }

    fun isAuthenticated(): Boolean {
        val token = getToken() ?: return false
        return try {
            val payload = decodeJwtPayload(token)
            val exp = payload.optLong("exp", 0)
            val now = System.currentTimeMillis() / 1000
            exp > now // retorna true se ainda não expirou
        } catch (e: Exception) {
            false
        }
    }

    fun getRole(): String? {
        val token = getToken() ?: return null
        return try {
            val payload = decodeJwtPayload(token)
            payload.optString("role", null)
        } catch (e: Exception) {
            null
        }
    }

    private fun decodeJwtPayload(jwt: String): JSONObject {
        val parts = jwt.split(".")
        if (parts.size != 3) throw IllegalArgumentException("JWT inválido")

        val payloadBytes = Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP)
        val payloadString = String(payloadBytes, Charsets.UTF_8)

        return JSONObject(payloadString)
    }
}
