package com.example.roteiro_pdm12.di

import android.content.Context
import androidx.room.Room
import com.example.roteiro_pdm12.data.local.dao.PartnerDao
import com.example.roteiro_pdm12.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "partner_hub_pdm12.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providePartnerDao(database: AppDatabase): PartnerDao {
        return database.partnerDao()
    }
}