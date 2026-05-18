package ucne.edu.registroocupacionesv2.presentation.empleados.list

import ucne.edu.registroocupacionesv2.domain.empleados.model.Empleado

data class EmpleadoListUiState(
    val isLoading: Boolean = false,
    val empleados: List<Empleado> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null
)
