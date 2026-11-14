package com.example.roteiro_pdm12.domain.model

data class CnpjLookupResult(
    val cnpj: String,
    val razaoSocial: String,
    val nomeFantasia: String,
    val ramoAtividade: String,
    val logradouro: String,
    val municipio: String,
    val uf: String,
    val cep: String,
    val telefone: String
)
