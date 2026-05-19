package ucne.edu.registroocupacionesv2.data.empleados.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EmpleadoDao {

    @Query(value = "SELECT * FROM empleados ORDER BY empleadoId DESC")
    fun observeAll(): Flow<List<EmpleadoEntity>>

    @Query(value = "SELECT * FROM empleados WHERE empleadoId = :id")
    fun getById(id: Int): EmpleadoEntity?

    @Upsert
    suspend fun upsert(entity: EmpleadoEntity)

    @Delete
    suspend fun delete(entity: EmpleadoEntity)

    @Query(value = "DELETE FROM empleados WHERE empleadoId = :id")
    suspend fun deleteById(id: Int)

    @Query(value = "SELECT EXISTS(SELECT 1 FROM empleados WHERE empleadoId = :id)")
    suspend fun exists(id: Int): Boolean
}