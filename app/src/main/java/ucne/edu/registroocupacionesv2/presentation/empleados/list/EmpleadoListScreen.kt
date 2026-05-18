package ucne.edu.registroocupacionesv2.presentation.empleados.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.registroocupacionesv2.domain.empleados.model.Empleado

@Composable
fun EmpleadoListScreen(
    viewModel: EmpleadoListViewModel = hiltViewModel(),
    onAddEmpleado: () -> Unit,
    onEditEmpleado: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    EmpleadoListBody(state, viewModel::onEvent, onAddEmpleado, onEditEmpleado)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoListBody(
    state: EmpleadoListUiState,
    onEvent: (EmpleadoListUiEvent) -> Unit,
    onAddEmpleado: () -> Unit,
    onEditEmpleado: (Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(EmpleadoListUiEvent.ClearMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Lista de Empleados") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddEmpleado,
                modifier = Modifier.testTag("fab_add")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar empleado"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("loading")
                )
            } else {
                if (state.empleados.isEmpty()) {
                    Text(
                        text = "No hay empleados registrados",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag("empty_message"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.empleados,
                            key = { it.empleadoId }
                        ) { empleado ->
                            EmpleadoItem(
                                empleado = empleado,
                                onEdit = { onEditEmpleado(empleado.empleadoId) },
                                onDelete = { onEvent(EmpleadoListUiEvent.Delete(empleado.empleadoId)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmpleadoItem(
    empleado: Empleado,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() }
            .testTag("empleado_item_${empleado.empleadoId}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = empleado.nombres,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Imprimimos el string del sexo directamente desde el modelo
                Text(
                    text = "Sexo: ${empleado.sexo} | Ingreso: ${empleado.fechaIngreso}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Sueldo: $${empleado.sueldo}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("btn_delete_${empleado.empleadoId}")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar empleado",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}