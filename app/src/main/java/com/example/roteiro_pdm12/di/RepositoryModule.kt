package com.example.roteiro_pdm12.di

import com.example.roteiro_pdm12.data.repository.PartnerRepositoryImpl
import com.example.roteiro_pdm12.domain.repository.PartnerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPartnerRepository(
        impl: PartnerRepositoryImpl
    ): PartnerRepository
}