package ucne.edu.registroocupacionesv2.domain.ocupaciones.usecase
import kotlinx.coroutines.flow.Flow
import ucne.edu.registroocupacionesv2.domain.ocupaciones.model.Ocupacion
import ucne.edu.registroocupacionesv2.domain.ocupaciones.repository.OcupacionRepository
import javax.inject.Inject
class ObserveOcupacionUseCase @Inject constructor(private val repository: OcupacionRepository) {
    operator fun invoke(): Flow<List<Ocupacion>> = repository.observeOcupaciones()
}