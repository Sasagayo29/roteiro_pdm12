package com.example.roteiro_pdm12.ui.screens.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.roteiro_pdm12.ui.screens.form.PartnerFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerFormScreen(
    onNavigateBack: () -> Unit,
    viewModel: PartnerFormViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSavedSuccess) {
        if (state.isSavedSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (state.isEditMode) "Editar Empresa" else "Cadastrar Empresa (API)")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Bloco de busca na BrasilAPI
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.cnpj,
                    onValueChange = { input ->
                        val digits = input.filter { it.isDigit() }.take(14)
                        viewModel.onFieldChange { it.copy(cnpj = digits) }
                    },
                    label = { Text("CNPJ (14 dígitos)") },
                    placeholder = { Text("Ex: 00000000000191") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Button(
                    onClick = viewModel::searchCnpjInBrasilApi,
                    enabled = !state.isLoadingApi && state.cnpj.length == 14,
                    modifier = Modifier.height(56.dp)
                ) {
                    if (state.isLoadingApi) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.CloudDownload, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Buscar API")
                    }
                }
            }

            state.apiStatusMessage?.let { msg ->
                Text(
                    text = msg,
                    color = if (state.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            OutlinedTextField(
                value = state.razaoSocial,
                onValueChange = { v -> viewModel.onFieldChange { it.copy(razaoSocial = v) } },
                label = { Text("Razão Social *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.nomeFantasia,
                onValueChange = { v -> viewModel.onFieldChange { it.copy(nomeFantasia = v) } },
                label = { Text("Nome Fantasia") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.ramoAtividade,
                onValueChange = { v -> viewModel.onFieldChange { it.copy(ramoAtividade = v) } },
                label = { Text("Ramo de Atividade (CNAE)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.logradouro,
                onValueChange = { v -> viewModel.onFieldChange { it.copy(logradouro = v) } },
                label = { Text("Logradouro e Número") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.municipio,
                    onValueChange = { v -> viewModel.onFieldChange { it.copy(municipio = v) } },
                    label = { Text("Município") },
                    modifier = Modifier.weight(2f)
                )
                OutlinedTextField(
                    value = state.uf,
                    onValueChange = { v -> viewModel.onFieldChange { it.copy(uf = v.take(2)) } },
                    label = { Text("UF") },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.cep,
                    onValueChange = { v -> viewModel.onFieldChange { it.copy(cep = v) } },
                    label = { Text("CEP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = state.telefone,
                    onValueChange = { v -> viewModel.onFieldChange { it.copy(telefone = v) } },
                    label = { Text("Telefone") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = state.contatoResponsavel,
                onValueChange = { v -> viewModel.onFieldChange { it.copy(contatoResponsavel = v) } },
                label = { Text("Responsável Comercial / Observações") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = viewModel::savePartner,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (state.isEditMode) "Salvar Alterações" else "Cadastrar Empresa")
            }
        }
    }
}