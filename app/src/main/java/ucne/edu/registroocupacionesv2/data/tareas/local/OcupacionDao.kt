package ucne.edu.registroocupacionesv2.data.tareas.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface OcupacionDao {
    @Query(value = "SELECT * FROM ocupaciones ORDER BY OcupacionId DESC")
    fun  observeAll(): Flow<List<OcupacionEntity>>

    @Query(value = "SELECT * From ocupaciones WHERE OcupacionId = :id")
    suspend fun getById(id: Int): OcupacionEntity?

    @Upsert
    suspend fun upsert(entity: OcupacionEntity)

    @Delete
    suspend fun delete(entity: OcupacionEntity)

    @Query("DELETE FROM ocupaciones WHERE OcupacionId = :id")
    suspend fun deleteById(id: Int)
}