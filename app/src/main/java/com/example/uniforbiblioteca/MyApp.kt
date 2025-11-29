package com.example.uniforbiblioteca

import android.app.Application
import com.example.uniforbiblioteca.api.UsuarioAPI
import com.example.uniforbiblioteca.viewmodel.FolderManager
import com.example.uniforbiblioteca.viewmodel.UsersManager

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FolderManager.initialize(this)
        UsersManager.initialize(this)
    }
}
