package com.example.roteiro_pdm12.data.remote.api

import com.example.roteiro_pdm12.data.remote.dto.CnpjResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface BrasilApiService {
    @GET("api/cnpj/v1/{cnpj}")
    suspend fun getCompanyByCnpj(
        @Path("cnpj") cnpj: String
    ): CnpjResponseDto
}