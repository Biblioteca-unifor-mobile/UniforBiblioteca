package com.example.uniforbiblioteca.viewmodel

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.example.uniforbiblioteca.R

object AccessibilityManager {

    private const val PREFS = "accessibility_prefs"
    private const val KEY_DYSLEXIA = "dyslexia"
    private const val KEY_DALTONIC = "daltonic"

    fun isDyslexiaEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_DYSLEXIA, false)
    }

    fun isDaltonicEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_DALTONIC, false)
    }

    fun setDyslexia(context: Context, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_DYSLEXIA, value).apply()
    }

    fun setDaltonic(context: Context, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_DALTONIC, value).apply()
    }

    fun applyDyslexiaFont(textView: TextView, context: Context) {
        if (isDyslexiaEnabled(context)) {
            val typeface = androidx.core.content.res.ResourcesCompat.getFont(
                context,
                R.font.opendyslexic
            )
            textView.typeface = typeface
        }
    }

    // ✅ Aplica leitura para daltônicos
    fun applyDaltonic(view: View, context: Context) {
        if (isDaltonicEnabled(context)) {
            view.setBackgroundColor(Color.WHITE)
        }
    }
}
