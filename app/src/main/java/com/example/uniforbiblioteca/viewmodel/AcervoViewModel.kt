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

    fun carregarLivros(reload: Boolean = false, onLoaded: (List<LivroData>) -> Unit, onError: (Throwable) -> Unit) {
        if (isLoaded && !reload) {
            onLoaded(acervo)
            return
        }

        if (reload) {
            page = 1
        }

        viewModelScope.launch {
            try {
                // Se for recarregar, talvez resetar paginação ou buscar tudo.
                // Aqui assumimos paginação simples (page 1)
                val response = livroAPI.getBooks(page = page, limit = 10)
                acervo = response.data ?: emptyList()
                if (acervo.isNotEmpty()) {
                    page++
                }
                isLoaded = true
                onLoaded(acervo)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
