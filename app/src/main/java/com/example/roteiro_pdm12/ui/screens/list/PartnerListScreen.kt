// PartnerListScreen.kt
package com.example.roteiro_pdm12.ui.screens.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.roteiro_pdm12.ui.screens.list.PartnerListViewModel
import com.example.roteiro_pdm12.domain.model.Partner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerListScreen(
    onAddPartner: () -> Unit,
    onEditPartner: (Long) -> Unit,
    onOpenTrash: () -> Unit,
    viewModel: PartnerListViewModel = hiltViewModel()
) {
    val partners by viewModel.partners.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Empresas Parceiras") },
                actions = {
                    IconButton(onClick = onOpenTrash) {
                        Icon(
                            imageVector = Icons.Default.RestoreFromTrash,
                            contentDescription = "Ver Lixeira (Soft Delete)"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddPartner,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nova Empresa") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Filtrar por Razão Social, Fantasia ou CNPJ...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            if (partners.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Nenhuma empresa ativa cadastrada.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 88.dp, top = 8.dp)
                ) {
                    items(partners, key = { it.id }) { partner ->
                        PartnerCard(
                            partner = partner,
                            onEdit = { onEditPartner(partner.id) },
                            onSoftDelete = { viewModel.softDeletePartner(partner.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PartnerCard(
    partner: Partner,
    onEdit: () -> Unit,
    onSoftDelete: () -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = partner.nomeFantasia.ifBlank { partner.razaoSocial },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = partner.razaoSocial,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onSoftDelete) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Mover para Lixeira (Soft Delete)",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "CNPJ: ${partner.cnpj}", style = MaterialTheme.typography.bodyMedium)
            if (partner.ramoAtividade.isNotBlank()) {
                Text(text = "CNAE: ${partner.ramoAtividade}", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "Local: ${partner.logradouro} - ${partner.municipio}/${partner.uf} (CEP: ${partner.cep})",
                style = MaterialTheme.typography.bodySmall
            )
            if (partner.contatoResponsavel.isNotBlank() || partner.telefone.isNotBlank()) {
                Text(
                    text = "Contato: ${partner.contatoResponsavel} • Tel: ${partner.telefone}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}