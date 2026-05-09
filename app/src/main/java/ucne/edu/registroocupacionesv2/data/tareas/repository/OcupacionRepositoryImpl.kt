package ucne.edu.registroocupacionesv2.data.tareas.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ucne.edu.registroocupacionesv2.data.tareas.local.OcupacionDao
import ucne.edu.registroocupacionesv2.data.tareas.mapper.toDomain
import ucne.edu.registroocupacionesv2.data.tareas.mapper.toEntity
import ucne.edu.registroocupacionesv2.domain.tareas.model.Ocupacion
import ucne.edu.registroocupacionesv2.domain.tareas.repository.OcupacionRepository
import javax.inject.Inject

class OcupacionRepositoryImpl @Inject constructor(private val localDataSource: OcupacionDao): OcupacionRepository{

    override fun observeOcupaciones(): Flow<List<Ocupacion>>
    {
        return  localDataSource.observeAll().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getOCupacion(id: Int): Ocupacion?
    {
        return localDataSource.getById(id)?.toDomain()
    }

    override suspend fun upsert(ocupacion: Ocupacion): Int
    {
        localDataSource.upsert(ocupacion.toEntity())
        return ocupacion.ocupacionId ?: 0
    }

    override suspend fun delete(id: Int)
    {
        localDataSource.deleteById(id)
    }

    override suspend fun exists(id: Int): Boolean
    {
        return localDataSource.exists(id)
    }

    override suspend fun existsByDescripcion(descripcion: String): Boolean
    {
        return localDataSource.existsByDescripcion(descripcion)
    }
}