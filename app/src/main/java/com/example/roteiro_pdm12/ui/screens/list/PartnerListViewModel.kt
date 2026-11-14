package com.example.roteiro_pdm12.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roteiro_pdm12.domain.model.Partner
import com.example.roteiro_pdm12.domain.usecase.GetActivePartnersUseCase
import com.example.roteiro_pdm12.domain.usecase.SoftDeletePartnerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerListViewModel @Inject constructor(
    getActivePartnersUseCase: GetActivePartnersUseCase,
    private val softDeletePartnerUseCase: SoftDeletePartnerUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val partners: StateFlow<List<Partner>> = combine(
        getActivePartnersUseCase(),
        _searchQuery
    ) { list, query ->
        if (query.isBlank()) {
            list
        } else {
            list.filter {
                it.razaoSocial.contains(query, ignoreCase = true) ||
                        it.nomeFantasia.contains(query, ignoreCase = true) ||
                        it.cnpj.contains(query)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun softDeletePartner(id: Long) {
        viewModelScope.launch {
            softDeletePartnerUseCase(id)
        }
    }
}