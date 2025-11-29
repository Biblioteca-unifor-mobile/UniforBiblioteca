package com.example.uniforbiblioteca.viewmodel

import com.example.uniforbiblioteca.dataclass.Folder
import com.example.uniforbiblioteca.dataclass.Loan
import com.example.uniforbiblioteca.dataclass.Reservation
import com.example.uniforbiblioteca.dataclass.Usuario

object UsersManager {

    var userList: MutableList<Usuario> = mutableListOf()

    var selectedUser: Usuario? = null

    var selectedUserFolders: MutableList<Folder> = mutableListOf()
    var selectedUserEmprestimos: MutableList<Loan> = mutableListOf()
    var selectedUserReservation: MutableList<Reservation> = mutableListOf()

    fun reset(){
        userList = mutableListOf()
        selectedUser = null
        selectedUserFolders = mutableListOf()
        selectedUserEmprestimos = mutableListOf()
        selectedUserReservation = mutableListOf()
    }
}