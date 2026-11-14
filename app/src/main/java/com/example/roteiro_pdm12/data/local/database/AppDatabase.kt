package com.example.roteiro_pdm12.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roteiro_pdm12.data.local.dao.PartnerDao
import com.example.roteiro_pdm12.data.local.entity.PartnerEntity

@Database(
    entities = [PartnerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun partnerDao(): PartnerDao
}