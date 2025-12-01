package com.example.uniforbiblioteca.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoanDetailsWrapper(
    @SerialName("loanId") val loanId: String? = null,
    @SerialName("dataEmprestimo") val dataEmprestimo: String? = null,
    @SerialName("dataLimite") val dataLimite: String? = null,
    @SerialName("diasAtraso") val diasAtraso: Int? = null,
    @SerialName("aluno") val aluno: AlunoInfo? = null
)
