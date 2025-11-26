package com.example.uniforbiblioteca.viewmodel

import android.R
import android.content.Context
import android.widget.Toast
import com.example.uniforbiblioteca.dataclass.Folder
import com.example.uniforbiblioteca.api.FolderAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.auth.AuthTokenHandler
import com.example.uniforbiblioteca.dataclass.FolderBook
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dataclass.Usuario

object FolderManager {

    val ROLE_LEITOR = 0
    val ROLE_EDITOR = 1
    val ROLE_PROPRITARIO = 2

    private lateinit var api: FolderAPI
    var currentFolder: Folder? = null
        private set

    var currentFolderId: String = ""
        private set

    var folderList: MutableList<Folder> = mutableListOf()





    fun initialize(context: Context) {
        api = RetrofitClient.create(context)
            .create(FolderAPI::class.java)
    }


    suspend fun deleteFolder(folder: Folder){
        if (folder.id == null) return
        api.deleteFolder(folder.id)
        updateFolderList()
    }

    suspend fun createFolder(folder: Folder){
        if (folder.nome == null) return
        api.createFolder(folder)
        updateFolderList()
    }
    suspend fun updateFolderList(){
        folderList = api.getMyFolders() as MutableList<Folder>
    }

    suspend fun changeFolderName(folder: Folder, newName: String){
        if (folder.id == null) return
        api.updateFolderName(folder.id, Folder(nome = newName))
        folder.nome = newName
    }

    fun getCurrentFolderBooks(): List<LivroData> {
        return currentFolder?.books.orEmpty()
    }


    suspend fun selectFolder(folderId: String): Folder {
        val folder = api.getFolderById(folderId)
        currentFolder = folder
        currentFolderId = folderId
        return folder
    }

    suspend fun deleteBookFromFolder(bookId: String): Boolean {
        if (currentFolderId.isBlank()) return false
        val folder = api.removeBookFromFolder(currentFolderId, bookId)
        currentFolder = folder
        return true
    }

    suspend fun addBookToFolder(folder: Folder, book: LivroData): Boolean {
        if (folder.id == null) return false
        if (book.id == null) return false
        val folder = api.addBookToFolder(folder.id, FolderBook(book.id))
        if (folder == currentFolder){
            currentFolder = folder
        }
        return true
    }

    suspend fun addUserToFolder(folder: Folder, user: Usuario): Boolean {
        if (folder.id == null) return false
        if (user.matricula == null){
            return false
        }
        val folder = api.removeUserFromFolder(currentFolderId, user.matricula)
        currentFolder = folder
        return true
    }

    suspend fun removeUserFromFolder(folder: Folder, user: Usuario): Boolean {
        if (currentFolderId.isBlank()) return false
        if (user.matricula == null) return false
        if (folder.id == null) return false
        val folder = api.addUserToFolder(folder.id, Usuario(user.matricula, role="LEITOR"))
        if (folder == currentFolder){
            currentFolder = folder
        }
        return true
    }

    fun checkUserFolderRole(context: Context, folder: Folder, requiredRole: Int): Boolean{

        //TODO MELHORAR ESSA LÓGICA HORRIVEL


        val tokenHandler = AuthTokenHandler(context)
        val matricula = tokenHandler.getMatricula()
        for (user in folder.users !!){
            if (matricula == user.matricula){
                var roleCode = 3
                Toast.makeText(context, user.role, Toast.LENGTH_SHORT).show()
                if (user.role != "PROPRIETARIO") roleCode -= 1
                if (user.role != "EDITOR") roleCode -= 1
                return roleCode >= requiredRole
            }
        }
        return false
    }

    fun clear() {
        currentFolder = null
        currentFolderId = ""
    }
}
