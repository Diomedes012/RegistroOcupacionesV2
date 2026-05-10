package ucne.edu.registroocupacionesv2.domain.ocupaciones.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.registroocupacionesv2.domain.ocupaciones.model.Ocupacion

interface OcupacionRepository {
    fun observeOcupaciones(): Flow<List<Ocupacion>>
    suspend fun getOCupacion(id: Int): Ocupacion?
    suspend fun  upsert(ocupacion: Ocupacion): Int
    suspend fun  delete(id: Int)
    suspend fun  exists(id: Int): Boolean
    suspend fun  existsByDescripcion(descripcion: String): Boolean
}