package ucne.edu.registroocupacionesv2.domain.tareas.usecase
import ucne.edu.registroocupacionesv2.domain.tareas.model.Ocupacion
import ucne.edu.registroocupacionesv2.domain.tareas.repository.OcupacionRepository
import javax.inject.Inject
class GetOcupacionUseCase @Inject constructor(private val repository: OcupacionRepository){
    suspend operator fun invoke(id: Int): Ocupacion? = repository.getOCupacion(id)
}