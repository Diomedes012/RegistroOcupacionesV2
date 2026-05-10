package ucne.edu.registroocupacionesv2.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ucne.edu.registroocupacionesv2.data.ocupaciones.local.OcupacionDao
import ucne.edu.registroocupacionesv2.data.ocupaciones.local.OcupacionEntity

@Database(
    entities = [OcupacionEntity::class],
    version = 1
)
abstract class OcupacionDb: RoomDatabase() {
    abstract fun ocupacionDao(): OcupacionDao
}