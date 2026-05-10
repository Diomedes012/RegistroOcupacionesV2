package ucne.edu.registroocupacionesv2.domain.ocupaciones.usecase

import ucne.edu.registroocupacionesv2.domain.ocupaciones.model.Ocupacion
import ucne.edu.registroocupacionesv2.domain.ocupaciones.repository.OcupacionRepository
import javax.inject.Inject

class UpsertOcupacionUseCase @Inject constructor(private val repository: OcupacionRepository) {
    suspend operator fun invoke(ocupacion: Ocupacion): Result<Int>
    {
        if(ocupacion.descripcion.isBlank())
        {
            return Result.failure(IllegalArgumentException("Descripcion es Obligatoria"))
        }

        if(ocupacion.sueldo <= 0.0)
        {
            return Result.failure(IllegalArgumentException("El sueldo debe ser mayor a cero"))
        }

        val existe = repository.existsByDescripcion(ocupacion.descripcion.trim())

        if(existe && ocupacion.ocupacionId == 0)
        {
            return Result.failure(IllegalArgumentException("Ya existe ocupacion con esta descripcion"))
        }

        return runCatching { repository.upsert(ocupacion) }
    }
}