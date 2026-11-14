package com.example.roteiro_pdm12.data.repository

import com.example.roteiro_pdm12.data.local.dao.PartnerDao
import com.example.roteiro_pdm12.data.mapper.toDomain
import com.example.roteiro_pdm12.data.mapper.toEntity
import com.example.roteiro_pdm12.data.remote.api.BrasilApiService
import com.example.roteiro_pdm12.domain.model.CnpjLookupResult
import com.example.roteiro_pdm12.domain.model.Partner
import com.example.roteiro_pdm12.domain.repository.PartnerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.collections.map

class PartnerRepositoryImpl @Inject constructor(
    private val dao: PartnerDao,
    private val apiService: BrasilApiService
) : PartnerRepository {

    override fun getActivePartners(): Flow<List<Partner>> {
        return dao.getActivePartners().map { list -> list.map { it.toDomain() } }
    }

    override fun getDeletedPartners(): Flow<List<Partner>> {
        return dao.getDeletedPartners().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getPartnerById(id: Long): Partner? {
        return dao.getPartnerById(id)?.toDomain()
    }

    override suspend fun savePartner(partner: Partner) {
        dao.upsertPartner(partner.toEntity())
    }

    override suspend fun softDeletePartner(id: Long) {
        dao.softDeleteById(id)
    }

    override suspend fun restorePartner(id: Long) {
        dao.restoreById(id)
    }

    override suspend fun fetchCompanyByCnpj(cnpj: String): Result<CnpjLookupResult> {
        return try {
            val response = apiService.getCompanyByCnpj(cnpj)
            Result.success(response.toDomain())
        } catch (e: HttpException) {
            val message = when (e.code()) {
                404 -> "CNPJ não encontrado na base da Receita Federal / BrasilAPI."
                400 -> "Formato de CNPJ inválido."
                else -> "Erro no servidor da API (Código ${e.code()})."
            }
            Result.failure(Exception(message))
        } catch (e: IOException) {
            Result.failure(Exception("Sem conexão com a internet. Verifique sua rede."))
        } catch (e: Exception) {
            Result.failure(Exception("Erro inesperado ao consultar CNPJ: ${e.localizedMessage}"))
        }
    }
}