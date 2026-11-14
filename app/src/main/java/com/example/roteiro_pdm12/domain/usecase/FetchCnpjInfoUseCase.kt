package com.example.roteiro_pdm12.domain.usecase

import com.example.roteiro_pdm12.domain.model.CnpjLookupResult
import com.example.roteiro_pdm12.domain.repository.PartnerRepository
import javax.inject.Inject

class FetchCnpjInfoUseCase @Inject constructor(
    private val repository: PartnerRepository
) {
    suspend operator fun invoke(rawCnpj: String): Result<CnpjLookupResult> {
        val cleanCnpj = rawCnpj.filter { it.isDigit() }
        if (cleanCnpj.length != 14) {
            return Result.failure(IllegalArgumentException("O CNPJ deve conter exatamente 14 dígitos numéricos."))
        }
        return repository.fetchCompanyByCnpj(cleanCnpj)
    }
}