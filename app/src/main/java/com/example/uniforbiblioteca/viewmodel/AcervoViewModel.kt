package com.example.uniforbiblioteca.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniforbiblioteca.api.LivroAPI
import com.example.uniforbiblioteca.dataclass.LivroData
import kotlinx.coroutines.launch

class AcervoViewModel(
    private val livroAPI: LivroAPI
) : ViewModel() {

    var acervo: List<LivroData> = emptyList()
        private set

    var page = 1
        private set

    var isLoaded = false
        private set

    fun carregarLivros(onLoaded: (List<LivroData>) -> Unit, onError: (Throwable) -> Unit) {
        if (isLoaded) {
            onLoaded(acervo)
            return
        }

        viewModelScope.launch {
            try {
                val response = livroAPI.getBooks(page = page, limit = 10)
                acervo = response.data ?: emptyList()
                page++
                isLoaded = true
                onLoaded(acervo)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
