package com.example.uniforbiblioteca.viewmodel

import android.content.Context

object FontManager {

    private const val PREFS = "font_prefs"
    private const val KEY_SCALE = "font_scale"

    private const val MIN = 0.8f
    private const val MAX = 1.4f
    private const val STEP = 0.1f

    fun getScale(context: Context): Float {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_SCALE, 1.0f)
    }

    private fun saveScale(context: Context, scale: Float) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putFloat(KEY_SCALE, scale).apply()
    }

    fun increase(context: Context) {
        val newValue = (getScale(context) + STEP).coerceAtMost(MAX)
        saveScale(context, newValue)
    }

    fun decrease(context: Context) {
        val newValue = (getScale(context) - STEP).coerceAtLeast(MIN)
        saveScale(context, newValue)
    }
}
