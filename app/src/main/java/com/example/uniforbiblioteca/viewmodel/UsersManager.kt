package com.example.uniforbiblioteca.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.uniforbiblioteca.api.FolderAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.api.UsuarioAPI
import com.example.uniforbiblioteca.dataclass.Folder
import com.example.uniforbiblioteca.dataclass.Loan
import com.example.uniforbiblioteca.dataclass.Reservation
import com.example.uniforbiblioteca.dataclass.Usuario

object UsersManager {

    var userList: MutableList<Usuario> = mutableListOf()

    private lateinit var api: UsuarioAPI
    var selectedUser: Usuario? = null

    var selectedUserFolders: MutableList<Folder> = mutableListOf()
    var selectedUserEmprestimos: MutableList<Loan> = mutableListOf()
    var selectedUserReservation: MutableList<Reservation> = mutableListOf()

    fun initialize(context: Context) {
        api = RetrofitClient.create(context)
            .create(UsuarioAPI::class.java)
    }

    suspend fun getUserList(){
        val resp = api.getUsers()
        Log.d("USER_MANAGER", resp.count.toString())
        userList = resp.data
    }

    fun reset(){
        userList = mutableListOf()
        selectedUser = null
        selectedUserFolders = mutableListOf()
        selectedUserEmprestimos = mutableListOf()
        selectedUserReservation = mutableListOf()
    }

    suspend fun deleteUser(user: Usuario){
        if (user.matricula == null) {
            return
        }
        api.deleteUser(user.matricula)
        reset()
    }

    suspend fun selectUser(user: Usuario): Boolean{
        if (user.matricula == null) return false
        try {
            val folders = api.getUserFolders(user.matricula)
            val loans = api.getUserLoans(user.matricula)
            val reservations = api.getUserReservations(user.matricula)
            selectedUser = user
            selectedUserFolders = folders.data
            selectedUserEmprestimos = loans.data
            selectedUserReservation = reservations.data
            return true
        } catch (e: Exception) {
            Log.e("SELECT_USER", e.message.toString())
            return false
        }
    }
}