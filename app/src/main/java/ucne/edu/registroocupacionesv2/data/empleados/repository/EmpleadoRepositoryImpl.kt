package ucne.edu.registroocupacionesv2.data.empleados.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ucne.edu.registroocupacionesv2.data.empleados.local.EmpleadoDao
import ucne.edu.registroocupacionesv2.data.empleados.mapper.toDomain
import ucne.edu.registroocupacionesv2.data.empleados.mapper.toEntity
import ucne.edu.registroocupacionesv2.domain.empleados.model.Empleado
import ucne.edu.registroocupacionesv2.domain.empleados.repository.EmpleadoRepository
import javax.inject.Inject

class EmpleadoRepositoryImpl @Inject constructor(private val localDataSource: EmpleadoDao): EmpleadoRepository
{
    override fun observeEmpleados(): Flow<List<Empleado>>
    {
        return localDataSource.observeAll().map { entities ->  entities.map { it.toDomain() } }
    }

    override suspend fun  getEmpleado(id: Int): Empleado?
    {
        return localDataSource.getById(id)?.toDomain()
    }

    override suspend fun upsert(empleado: Empleado): Int
    {
        localDataSource.upsert(empleado.toEntity())
        return empleado.empleadoId ?: 0
    }

    override suspend fun delete(id: Int)
    {
        return localDataSource.deleteById(id)
    }

    override suspend fun  exists(id: Int): Boolean
    {
        return localDataSource.exists(id)
    }
}