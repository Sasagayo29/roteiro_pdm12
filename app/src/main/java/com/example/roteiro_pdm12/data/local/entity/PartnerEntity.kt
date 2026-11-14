package com.example.roteiro_pdm12.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partners")
data class PartnerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val cnpj: String,
    @ColumnInfo(name = "razao_social") val razaoSocial: String,
    @ColumnInfo(name = "nome_fantasia") val nomeFantasia: String,
    @ColumnInfo(name = "ramo_atividade") val ramoAtividade: String,
    val logradouro: String,
    val municipio: String,
    val uf: String,
    val cep: String,
    val telefone: String,
    @ColumnInfo(name = "contato_responsavel") val contatoResponsavel: String,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean = false
)
