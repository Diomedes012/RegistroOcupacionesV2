package ucne.edu.registroocupacionesv2.data.ocupaciones.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface OcupacionDao {
    @Query(value = "SELECT * FROM ocupaciones ORDER BY ocupacionId DESC")
    fun  observeAll(): Flow<List<OcupacionEntity>>

    @Query(value = "SELECT * From ocupaciones WHERE ocupacionId = :id")
    suspend fun getById(id: Int): OcupacionEntity?

    @Upsert
    suspend fun upsert(entity: OcupacionEntity)

    @Delete
    suspend fun delete(entity: OcupacionEntity)

    @Query("DELETE FROM ocupaciones WHERE ocupacionId = :id")
    suspend fun deleteById(id: Int)

    @Query(value = "SELECT EXISTS(SELECT 1 FROM ocupaciones WHERE ocupacionId = :id)")
    suspend fun exists(id: Int): Boolean

    @Query(value = "SELECT EXISTS(SELECT 1 FROM ocupaciones WHERE descripcion = :descripcion)")
    suspend fun existsByDescripcion(descripcion: String): Boolean
}