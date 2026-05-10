package ucne.edu.registroocupacionesv2.presentation.ocupaciones.list

sealed interface OcupacionListUiEvent {
    data object Load: OcupacionListUiEvent
    data object Refresh: OcupacionListUiEvent
    data class Delete(val id: Int): OcupacionListUiEvent
    data class  ShowMessage(val message: String): OcupacionListUiEvent
    data object ClearMessage: OcupacionListUiEvent
    data object  CreateNew: OcupacionListUiEvent
    data class Edit(val id: Int): OcupacionListUiEvent
}