package ucne.edu.registroocupacionesv2.domain.empleados.usecase

import ucne.edu.registroocupacionesv2.domain.empleados.repository.EmpleadoRepository
import javax.inject.Inject

class DeleteEmpleadoUseCase @Inject constructor(private val repository: EmpleadoRepository)
{
    suspend operator fun invoke(id: Int) = repository.delete(id)
}