package ucne.edu.registroocupacionesv2.presentation.empleados.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.registroocupacionesv2.domain.empleados.model.Empleado
import ucne.edu.registroocupacionesv2.domain.empleados.usecase.DeleteEmpleadoUseCase
import ucne.edu.registroocupacionesv2.domain.empleados.usecase.GetEmpleadoUseCase
import ucne.edu.registroocupacionesv2.domain.empleados.usecase.UpsertEmpleadoUseCase
import ucne.edu.registroocupacionesv2.presentation.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class EmpleadoFormViewModel @Inject constructor(
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val upsertEmpleadoUseCase: UpsertEmpleadoUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val routeArgs = savedStateHandle.toRoute<Screen.EmpleadoForm>()
    private val empleadoId: Int = routeArgs.empleadoId

    private val _state = MutableStateFlow(EmpleadoFormUiState())
    val state: StateFlow<EmpleadoFormUiState> = _state.asStateFlow()

    init {
        loadEmpleado(empleadoId)
    }

    fun onEvent(event: EmpleadoFormUiEvent) {
        when (event) {
            is EmpleadoFormUiEvent.Load -> loadEmpleado(event.id)
            is EmpleadoFormUiEvent.NombresChanged -> _state.update {
                it.copy(nombres = event.value, nombresError = null)
            }
            is EmpleadoFormUiEvent.SexoChanged -> _state.update {
                it.copy(sexo = event.value, sexoError = null)
            }
            is EmpleadoFormUiEvent.FechaIngresoChanged -> _state.update {
                it.copy(fechaIngreso = event.value, fechaIngresoError = null)
            }
            is EmpleadoFormUiEvent.SueldoChanged -> _state.update {
                it.copy(sueldo = event.value, sueldoError = null)
            }
            EmpleadoFormUiEvent.Save -> onSave()
            EmpleadoFormUiEvent.Delete -> onDelete()
        }
    }

    private fun loadEmpleado(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, empleadoId = null) }
            return
        }

        viewModelScope.launch {
            val empleado = getEmpleadoUseCase(id)
            if (empleado != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        empleadoId = empleado.empleadoId,
                        nombres = empleado.nombres,
                        sexo = empleado.sexo,
                        fechaIngreso = empleado.fechaIngreso,
                        sueldo = empleado.sueldo.toString()
                    )
                }
            } else {
                _state.update { it.copy(isNew = true, empleadoId = null) }
            }
        }
    }

    private fun onSave() {
        val nombres = state.value.nombres.trim()
        val sexo = state.value.sexo.trim()
        val fechaIngreso = state.value.fechaIngreso.trim()
        val sueldoText = state.value.sueldo.trim()

        var hasError = false
        _state.update { current ->
            current.copy(
                nombresError = if (nombres.isBlank()) "Obligatorio" else null,
                sexoError = if (sexo.isBlank()) "Obligatorio" else null,
                fechaIngresoError = if (fechaIngreso.isBlank()) "Obligatorio" else null,
                sueldoError = if (sueldoText.toDoubleOrNull() == null) "Sueldo inválido" else null
            ).also { newState ->
                hasError = newState.nombresError != null || newState.sexoError != null ||
                        newState.fechaIngresoError != null || newState.sueldoError != null
            }
        }

        if (hasError) return

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val empleado = Empleado(
                empleadoId = state.value.empleadoId ?: 0,
                nombres = nombres,
                sexo = sexo,
                fechaIngreso = fechaIngreso,
                sueldo = sueldoText.toDouble()
            )

            val result = upsertEmpleadoUseCase(empleado)

            result.onSuccess { newId ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        saved = true,
                        empleadoId = newId,
                        isNew = false
                    )
                }
            }.onFailure { exception ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        nombresError = exception.message
                    )
                }
            }
        }
    }

    private fun onDelete() {
        val id = state.value.empleadoId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteEmpleadoUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}