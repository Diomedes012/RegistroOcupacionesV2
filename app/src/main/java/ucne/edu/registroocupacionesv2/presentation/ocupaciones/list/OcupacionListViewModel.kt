package ucne.edu.registroocupacionesv2.presentation.ocupaciones.list

import androidx.collection.emptyLongSet
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ucne.edu.registroocupacionesv2.domain.ocupaciones.usecase.DeleteOcupacionUseCase
import ucne.edu.registroocupacionesv2.domain.ocupaciones.usecase.ObserveOcupacionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.annotation.meta.When
import javax.inject.Inject

@HiltViewModel
class OcupacionListViewModel @Inject constructor(
    private val obseveOcupacionesUseCase: ObserveOcupacionUseCase,
    private val deleteOCupacionuseCase: DeleteOcupacionUseCase
): ViewModel()
{
    private val _state = MutableStateFlow(OcupacionListUiState(isLoading = true))

    val state: StateFlow<OcupacionListUiState> = _state.asStateFlow()

    init
    {
        loadOcupaciones()
    }

    fun onEvent(event: OcupacionListUiEvent)
    {
        when(event)
        {
            OcupacionListUiEvent.Load -> loadOcupaciones()
            OcupacionListUiEvent.Refresh -> loadOcupaciones()
            is OcupacionListUiEvent.Delete -> onDelete(event.id)
            is OcupacionListUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            OcupacionListUiEvent.ClearMessage -> _state.update { it.copy(navigateToCreate = true) }
            is OcupacionListUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            else -> {}
        }
    }

    fun loadOcupaciones()
    {
        viewModelScope.launch{
            _state.update { it.copy(isLoading = true) }
            obseveOcupacionesUseCase().collectLatest{ list ->
                _state.update { it.copy(isLoading = false, ocupaciones = list, message = null) }
            }
        }
    }

    private fun onDelete(id: Int)
    {
        viewModelScope.launch{
            deleteOCupacionuseCase(id)
            onEvent(OcupacionListUiEvent.ShowMessage("Ocupacion Eliminada"))
        }
    }

    fun onNavigationDone()
    {
        _state.update { it.copy(navigateToCreate = false, navigateToEditId = null) }
    }
}