package com.example.roteiro_pdm12.domain.usecase

import com.example.roteiro_pdm12.domain.model.Partner
import com.example.roteiro_pdm12.domain.repository.PartnerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActivePartnersUseCase @Inject constructor(
    private val repository: PartnerRepository
) {
    operator fun invoke(): Flow<List<Partner>> = repository.getActivePartners()
}

class GetDeletedPartnersUseCase @Inject constructor(
    private val repository: PartnerRepository
) {
    operator fun invoke(): Flow<List<Partner>> = repository.getDeletedPartners()
}

class GetPartnerByIdUseCase @Inject constructor(
    private val repository: PartnerRepository
) {
    suspend operator fun invoke(id: Long): Partner? = repository.getPartnerById(id)
}

class SavePartnerUseCase @Inject constructor(
    private val repository: PartnerRepository
) {
    suspend operator fun invoke(partner: Partner): Result<Unit> {
        if (partner.razaoSocial.isBlank()) {
            return Result.failure(IllegalArgumentException("A Razão Social é obrigatória."))
        }
        if (partner.cnpj.filter { it.isDigit() }.length != 14) {
            return Result.failure(IllegalArgumentException("Informe um CNPJ válido com 14 dígitos."))
        }
        repository.savePartner(partner)
        return Result.success(Unit)
    }
}

class SoftDeletePartnerUseCase @Inject constructor(
    private val repository: PartnerRepository
) {
    suspend operator fun invoke(id: Long) = repository.softDeletePartner(id)
}

class RestorePartnerUseCase @Inject constructor(
    private val repository: PartnerRepository
) {
    suspend operator fun invoke(id: Long) = repository.restorePartner(id)
}