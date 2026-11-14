package com.example.roteiro_pdm12.ui.screens.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roteiro_pdm12.domain.model.Partner
import com.example.roteiro_pdm12.domain.usecase.GetDeletedPartnersUseCase
import com.example.roteiro_pdm12.domain.usecase.RestorePartnerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerTrashViewModel @Inject constructor(
    getDeletedPartnersUseCase: GetDeletedPartnersUseCase,
    private val restorePartnerUseCase: RestorePartnerUseCase
) : ViewModel() {

    val deletedPartners: StateFlow<List<Partner>> = getDeletedPartnersUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun restorePartner(id: Long) {
        viewModelScope.launch {
            restorePartnerUseCase(id)
        }
    }
}