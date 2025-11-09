package com.example.uniforbiblioteca.dataclass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LivroData(
    @SerialName("_id") var id: String? = null,
    @SerialName("_isbn") var isbn: String? = null,
    @SerialName("_titulo") var titulo: String? = null,
    @SerialName("_autor") var autor: String? = null,
    @SerialName("_coAutores") var coAutores: List<String>? = null,
    @SerialName("_edicao") var edicao: String? = null,
    @SerialName("_anoEdicao") var anoEdicao: Int? = null,
    @SerialName("_idioma") var idioma: String? = null,
    @SerialName("_publicacao") var publicacao: String? = null,
    @SerialName("_resumo") var resumo: String? = null,
    @SerialName("_imageUrl") var imageUrl: String? = null,
    @SerialName("_tipo") var tipo: String? = null,
    @SerialName("_numeroExemplares") var numeroExemplares: Int? = null,
    @SerialName("_copies") var copies: List<ExemplarData>? = null

)


