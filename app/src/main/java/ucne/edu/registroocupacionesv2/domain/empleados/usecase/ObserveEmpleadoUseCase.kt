package ucne.edu.registroocupacionesv2.domain.empleados.usecase

import kotlinx.coroutines.flow.Flow
import ucne.edu.registroocupacionesv2.domain.empleados.model.Empleado
import ucne.edu.registroocupacionesv2.domain.empleados.repository.EmpleadoRepository
import javax.inject.Inject

class ObserveEmpleadoUseCase @Inject constructor(private val repository: EmpleadoRepository)
{
    operator fun invoke(): Flow<List<Empleado>> = repository.observeEmpleados()
}