package ucne.edu.registroocupacionesv2.presentation.ocupaciones.form

import androidx.compose.runtime.MutableState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ucne.edu.registroocupacionesv2.domain.ocupaciones.usecase.DeleteOcupacionUseCase
import ucne.edu.registroocupacionesv2.domain.ocupaciones.usecase.GetOcupacionUseCase
import androidx.navigation.toRoute
import androidx.room.RoomOpenDelegate
import androidx.room.util.EMPTY_STRING_ARRAY
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ucne.edu.registroocupacionesv2.domain.ocupaciones.model.Ocupacion
import ucne.edu.registroocupacionesv2.domain.ocupaciones.usecase.UpsertOcupacionUseCase
import ucne.edu.registroocupacionesv2.presentation.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class OcupacionFormViewModel @Inject constructor(
    private val getOcupacionUseCase: GetOcupacionUseCase,
    private val upsertOcupacionUseCase: UpsertOcupacionUseCase,
    private val deleteOcupacionUseCase: DeleteOcupacionUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel()
{
    private val routeArgs = savedStateHandle.toRoute<Screen.OcupacionForm>()
    private val ocupacionId: Int = routeArgs.ocupacionId

    private val _state = MutableStateFlow(OcupacionFormUiState())
    val state: StateFlow<OcupacionFormUiState> = _state.asStateFlow()

    init {
        loadOcupacion(ocupacionId)
    }

    fun onEvent(event: OcupacionFormUiEvent)
    {
        when(event)
        {
            is OcupacionFormUiEvent.Load -> loadOcupacion(event.id)
            is OcupacionFormUiEvent.DescripcionChanged -> _state.update {
                it.copy(descripcion = event.value, descripcionError = null)
            }
            is OcupacionFormUiEvent.SueldoChanged -> _state.update {
                it.copy(sueldo = event.value, sueldoError = null)
            }
            OcupacionFormUiEvent.Save -> onSave()
            OcupacionFormUiEvent.Delete -> onDelete()
        }
    }

    private fun loadOcupacion(id: Int?)
    {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, ocupacionId = null) }
            return
        }

        viewModelScope.launch {
            val ocupacion = getOcupacionUseCase(id)
            if(ocupacion != null)
            {
                _state.update {
                    it.copy(
                        isNew = false,
                        ocupacionId = ocupacion.ocupacionId,
                        descripcion = ocupacion.descripcion,
                        sueldo = ocupacion.sueldo.toString()
                    )
                }
            }else
            {
                _state.update { it.copy(isNew = true, ocupacionId = null) }
            }
        }
    }

    private fun onSave()
    {
        val descripcion = state.value.descripcion
        val sueldoText = state.value.sueldo

        val descripcionValidation = validateDescripcion(descripcion)
        val sueldoValidation = validateSueldo(sueldoText)

        if(!descripcionValidation.isValid || !sueldoValidation.isValid)
        {
            _state.update {
                it.copy(
                    descripcionError = descripcionValidation.error,
                    sueldoError = sueldoValidation.error
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val ocupacion = Ocupacion(
                ocupacionId = state.value.ocupacionId ?: 0,
                descripcion = descripcion,
                sueldo = sueldoText.toDouble()
            )

            val result = upsertOcupacionUseCase(ocupacion)

            result.onSuccess {
                newId -> _state.update {
                    it.copy(
                        isSaving = false,
                        saved = true,
                        ocupacionId = newId,
                        isNew = false
                    )
            }
            }.onFailure {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun onDelete()
    {
        val id = state.value.ocupacionId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteOcupacionUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }

    private fun validateDescripcion(descripcion: String): ValidationResult
    {

        if(descripcion.isBlank())
        {
            return ValidationResult(isValid = false, error = "La descripcion no puede estar vacio")
        }
        return ValidationResult(isValid = true)
    }

    private fun validateSueldo(sueldo: String): ValidationResult
    {
        if(sueldo.isBlank())
        {
            return ValidationResult(isValid = false, error = "Le sueldo no puede estar vacio")
        }

        val sueldoDouble = sueldo.toDoubleOrNull()

        if(sueldoDouble == null || sueldoDouble <= 0)
        {
            return ValidationResult(isValid = false, error = "Ingrese un monto valido mayor a 0")
        }
        return ValidationResult(isValid = true)
    }


}

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)