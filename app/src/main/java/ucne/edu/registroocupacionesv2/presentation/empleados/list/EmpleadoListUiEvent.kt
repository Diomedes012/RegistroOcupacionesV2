package ucne.edu.registroocupacionesv2.presentation.empleados.list

sealed class EmpleadoListUiEvent {
    data object Load : EmpleadoListUiEvent()
    data object Refresh : EmpleadoListUiEvent()
    data class Delete(val id: Int) : EmpleadoListUiEvent()
    data class ShowMessage(val message: String) : EmpleadoListUiEvent()
    data object ClearMessage : EmpleadoListUiEvent()
    data object CreateNew : EmpleadoListUiEvent()
    data class Edit(val id: Int) : EmpleadoListUiEvent()
}