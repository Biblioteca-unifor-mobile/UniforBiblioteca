package com.example.uniforbiblioteca

import android.app.Application
import com.example.uniforbiblioteca.viewmodel.FolderManager

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FolderManager.initialize(this)
    }
}
