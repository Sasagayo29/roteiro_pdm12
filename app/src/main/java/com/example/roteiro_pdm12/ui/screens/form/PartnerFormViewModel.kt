package com.example.roteiro_pdm12.ui.screens.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roteiro_pdm12.domain.model.Partner
import com.example.roteiro_pdm12.domain.usecase.FetchCnpjInfoUseCase
import com.example.roteiro_pdm12.domain.usecase.GetPartnerByIdUseCase
import com.example.roteiro_pdm12.domain.usecase.SavePartnerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PartnerFormUiState(
    val id: Long = 0L,
    val cnpj: String = "",
    val razaoSocial: String = "",
    val nomeFantasia: String = "",
    val ramoAtividade: String = "",
    val logradouro: String = "",
    val municipio: String = "",
    val uf: String = "",
    val cep: String = "",
    val telefone: String = "",
    val contatoResponsavel: String = "",
    val isLoadingApi: Boolean = false,
    val apiStatusMessage: String? = null,
    val isError: Boolean = false,
    val isSavedSuccess: Boolean = false
) {
    val isEditMode: Boolean get() = id > 0L
}

@HiltViewModel
class PartnerFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fetchCnpjInfoUseCase: FetchCnpjInfoUseCase,
    private val getPartnerByIdUseCase: GetPartnerByIdUseCase,
    private val savePartnerUseCase: SavePartnerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PartnerFormUiState())
    val uiState: StateFlow<PartnerFormUiState> = _uiState.asStateFlow()

    init {
        val partnerId = savedStateHandle.get<Long>("partnerId") ?: -1L
        if (partnerId > 0L) {
            loadExistingPartner(partnerId)
        }
    }

    private fun loadExistingPartner(id: Long) {
        viewModelScope.launch {
            getPartnerByIdUseCase(id)?.let { partner ->
                _uiState.update {
                    it.copy(
                        id = partner.id,
                        cnpj = partner.cnpj,
                        razaoSocial = partner.razaoSocial,
                        nomeFantasia = partner.nomeFantasia,
                        ramoAtividade = partner.ramoAtividade,
                        logradouro = partner.logradouro,
                        municipio = partner.municipio,
                        uf = partner.uf,
                        cep = partner.cep,
                        telefone = partner.telefone,
                        contatoResponsavel = partner.contatoResponsavel
                    )
                }
            }
        }
    }

    fun onFieldChange(updateBlock: (PartnerFormUiState) -> PartnerFormUiState) {
        _uiState.update { current ->
            updateBlock(current).copy(apiStatusMessage = null, isError = false)
        }
    }

    fun searchCnpjInBrasilApi() {
        val currentCnpj = _uiState.value.cnpj
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingApi = true,
                    apiStatusMessage = "Consultando CNPJ na BrasilAPI...",
                    isError = false
                )
            }

            val result = fetchCnpjInfoUseCase(currentCnpj)
            result.fold(
                onSuccess = { info ->
                    _uiState.update {
                        it.copy(
                            isLoadingApi = false,
                            cnpj = info.cnpj.ifBlank { currentCnpj },
                            razaoSocial = info.razaoSocial,
                            nomeFantasia = info.nomeFantasia,
                            ramoAtividade = info.ramoAtividade,
                            logradouro = info.logradouro,
                            municipio = info.municipio,
                            uf = info.uf,
                            cep = info.cep,
                            telefone = info.telefone,
                            apiStatusMessage = "Dados importados com sucesso da BrasilAPI!",
                            isError = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoadingApi = false,
                            apiStatusMessage = error.message ?: "Falha na consulta.",
                            isError = true
                        )
                    }
                }
            )
        }
    }

    fun savePartner() {
        val state = _uiState.value
        viewModelScope.launch {
            val partner = Partner(
                id = state.id,
                cnpj = state.cnpj.filter { it.isDigit() },
                razaoSocial = state.razaoSocial.trim(),
                nomeFantasia = state.nomeFantasia.trim(),
                ramoAtividade = state.ramoAtividade.trim(),
                logradouro = state.logradouro.trim(),
                municipio = state.municipio.trim(),
                uf = state.uf.trim().uppercase(),
                cep = state.cep.trim(),
                telefone = state.telefone.trim(),
                contatoResponsavel = state.contatoResponsavel.trim()
            )

            savePartnerUseCase(partner).fold(
                onSuccess = {
                    _uiState.update { it.copy(isSavedSuccess = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            apiStatusMessage = error.message,
                            isError = true
                        )
                    }
                }
            )
        }
    }
}