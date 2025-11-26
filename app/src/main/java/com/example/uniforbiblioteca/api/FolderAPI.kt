package com.example.uniforbiblioteca.api

import com.example.uniforbiblioteca.dataclass.Folder
import com.example.uniforbiblioteca.dataclass.FolderBook
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dataclass.Usuario
import retrofit2.http.*

interface FolderAPI {

    @POST("folders")
    suspend fun createFolder(
        @Body body: Folder
    ): Folder

    @GET("folders/my")
    suspend fun getMyFolders(
    ): List<Folder>

    @GET("folders/{id}")
    suspend fun getFolderById(
        @Path("id") id: String
    ): Folder

    @PATCH("folders/{id}/name")
    suspend fun updateFolderName(
        @Path("id") id: String,
        @Body body: Folder
    ): Folder

    @DELETE("folders/{id}")
    suspend fun deleteFolder(
        @Path("id") id: String
    ): Folder

    // --- Books ---

    @POST("folders/{id}/books")
    suspend fun addBookToFolder(
        @Path("id") folderId: String,
        @Body body: FolderBook
    ): Folder

    @DELETE("folders/{id}/books/{bookId}")
    suspend fun removeBookFromFolder(
        @Path("id") folderId: String,
        @Path("bookId") bookId: String
    ): Folder

    // --- Users ---

    @POST("folders/{id}/users")
    suspend fun addUserToFolder(
        @Path("id") folderId: String,
        @Body body: Usuario
    ): Folder

    @DELETE("folders/{id}/users/{userMatricula}")
    suspend fun removeUserFromFolder(
        @Path("id") folderId: String,
        @Path("userMatricula") userMatricula: String
    ): Folder
}
