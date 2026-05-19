package ucne.edu.registroocupacionesv2.domain.empleados.usecase

import ucne.edu.registroocupacionesv2.domain.empleados.model.Empleado
import ucne.edu.registroocupacionesv2.domain.empleados.repository.EmpleadoRepository
import javax.inject.Inject
class GetEmpleadoUseCase @Inject constructor(private val repository: EmpleadoRepository)
{
    suspend operator fun invoke(id: Int): Empleado? = repository.getEmpleado(id)
}