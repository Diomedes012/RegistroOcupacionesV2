package ucne.edu.registroocupacionesv2.domain.tareas.usecase

import ucne.edu.registroocupacionesv2.domain.tareas.repository.OcupacionRepository
import javax.inject.Inject
class DeleteOcupacionUseCase @Inject constructor(private val repository: OcupacionRepository){
    suspend operator fun invoke(id: Int) = repository.delete(id)
}