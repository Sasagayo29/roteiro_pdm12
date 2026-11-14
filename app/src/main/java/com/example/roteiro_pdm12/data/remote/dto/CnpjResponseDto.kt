package com.example.roteiro_pdm12.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CnpjResponseDto(
    @SerializedName("cnpj") val cnpj: String?,
    @SerializedName("razao_social") val razaoSocial: String?,
    @SerializedName("nome_fantasia") val nomeFantasia: String?,
    @SerializedName("cnae_fiscal_descricao") val cnaeDescricao: String?,
    @SerializedName("logradouro") val logradouro: String?,
    @SerializedName("numero") val numero: String?,
    @SerializedName("municipio") val municipio: String?,
    @SerializedName("uf") val uf: String?,
    @SerializedName("cep") val cep: String?,
    @SerializedName("ddd_telefone_1") val telefone: String?
)
