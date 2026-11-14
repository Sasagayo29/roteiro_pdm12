package com.example.roteiro_pdm12.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.roteiro_pdm12.data.local.entity.PartnerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PartnerDao {

    @Query("SELECT * FROM partners WHERE is_deleted = 0 ORDER BY razao_social ASC")
    fun getActivePartners(): Flow<List<PartnerEntity>>

    @Query("SELECT * FROM partners WHERE is_deleted = 1 ORDER BY razao_social ASC")
    fun getDeletedPartners(): Flow<List<PartnerEntity>>

    @Query("SELECT * FROM partners WHERE id = :id LIMIT 1")
    suspend fun getPartnerById(id: Long): PartnerEntity?

    // @Upsert insere se o id for 0 ou atualiza se o id já existir no banco (Edição)
    @Upsert
    suspend fun upsertPartner(partner: PartnerEntity)

    // Soft Delete: marca como excluído sem apagar a linha da tabela
    @Query("UPDATE partners SET is_deleted = 1 WHERE id = :id")
    suspend fun softDeleteById(id: Long)

    // Restaurar registro excluído logicamente
    @Query("UPDATE partners SET is_deleted = 0 WHERE id = :id")
    suspend fun restoreById(id: Long)
}