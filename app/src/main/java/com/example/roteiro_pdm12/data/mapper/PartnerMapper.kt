package com.example.roteiro_pdm12.data.mapper

import com.example.roteiro_pdm12.data.local.entity.PartnerEntity
import com.example.roteiro_pdm12.data.remote.dto.CnpjResponseDto
import com.example.roteiro_pdm12.domain.model.CnpjLookupResult
import com.example.roteiro_pdm12.domain.model.Partner

fun PartnerEntity.toDomain(): Partner = Partner(
    id = id,
    cnpj = cnpj,
    razaoSocial = razaoSocial,
    nomeFantasia = nomeFantasia,
    ramoAtividade = ramoAtividade,
    logradouro = logradouro,
    municipio = municipio,
    uf = uf,
    cep = cep,
    telefone = telefone,
    contatoResponsavel = contatoResponsavel,
    isDeleted = isDeleted
)

fun Partner.toEntity(): PartnerEntity = PartnerEntity(
    id = id,
    cnpj = cnpj,
    razaoSocial = razaoSocial,
    nomeFantasia = nomeFantasia,
    ramoAtividade = ramoAtividade,
    logradouro = logradouro,
    municipio = municipio,
    uf = uf,
    cep = cep,
    telefone = telefone,
    contatoResponsavel = contatoResponsavel,
    isDeleted = isDeleted
)

fun CnpjResponseDto.toDomain(): CnpjLookupResult {
    val enderecoFormatado = listOfNotNull(
        logradouro?.takeIf { it.isNotBlank() },
        numero?.takeIf { it.isNotBlank() }
    ).joinToString(", ")

    return CnpjLookupResult(
        cnpj = cnpj.orEmpty(),
        razaoSocial = razaoSocial.orEmpty(),
        nomeFantasia = nomeFantasia?.takeIf { it.isNotBlank() } ?: razaoSocial.orEmpty(),
        ramoAtividade = cnaeDescricao.orEmpty(),
        logradouro = enderecoFormatado,
        municipio = municipio.orEmpty(),
        uf = uf.orEmpty(),
        cep = cep.orEmpty(),
        telefone = telefone.orEmpty()
    )
}