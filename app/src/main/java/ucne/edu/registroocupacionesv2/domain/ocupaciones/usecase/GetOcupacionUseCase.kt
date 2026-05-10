package ucne.edu.registroocupacionesv2.domain.ocupaciones.usecase
import ucne.edu.registroocupacionesv2.domain.ocupaciones.model.Ocupacion
import ucne.edu.registroocupacionesv2.domain.ocupaciones.repository.OcupacionRepository
import javax.inject.Inject
class GetOcupacionUseCase @Inject constructor(private val repository: OcupacionRepository){
    suspend operator fun invoke(id: Int): Ocupacion? = repository.getOCupacion(id)
}