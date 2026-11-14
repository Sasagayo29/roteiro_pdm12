package com.example.roteiro_pdm12.domain.repository

import com.example.roteiro_pdm12.domain.model.CnpjLookupResult
import com.example.roteiro_pdm12.domain.model.Partner
import kotlinx.coroutines.flow.Flow

interface PartnerRepository {
    fun getActivePartners(): Flow<List<Partner>>
    fun getDeletedPartners(): Flow<List<Partner>>
    suspend fun getPartnerById(id: Long): Partner?
    suspend fun savePartner(partner: Partner)
    suspend fun softDeletePartner(id: Long)
    suspend fun restorePartner(id: Long)
    suspend fun fetchCompanyByCnpj(cnpj: String): Result<CnpjLookupResult>
}