package ucne.edu.registroocupacionesv2.presentation.ocupaciones.list

import ucne.edu.registroocupacionesv2.domain.ocupaciones.model.Ocupacion

data class OcupacionListUiState(
    val isLoading: Boolean = false,
    val ocupaciones: List<Ocupacion> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null
)
