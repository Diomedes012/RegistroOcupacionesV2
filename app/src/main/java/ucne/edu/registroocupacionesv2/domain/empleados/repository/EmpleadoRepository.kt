package ucne.edu.registroocupacionesv2.domain.empleados.repository

import ucne.edu.registroocupacionesv2.data.empleados.local.EmpleadoEntity
import kotlinx.coroutines.flow.Flow
import ucne.edu.registroocupacionesv2.domain.empleados.model.Empleado

interface EmpleadoRepository {
    fun observeEmpleados(): Flow<List<Empleado>>
    suspend fun  getEmpleado(id: Int): Empleado?
    suspend fun upsert(empleado: Empleado): Int
    suspend fun delete(id: Int)
    suspend fun  exists(id: Int): Boolean
}