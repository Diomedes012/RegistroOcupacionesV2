package ucne.edu.registroocupacionesv2.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ucne.edu.registroocupacionesv2.data.empleados.local.EmpleadoDao
import ucne.edu.registroocupacionesv2.data.empleados.local.EmpleadoEntity
import ucne.edu.registroocupacionesv2.data.ocupaciones.local.OcupacionDao
import ucne.edu.registroocupacionesv2.data.ocupaciones.local.OcupacionEntity

@Database(
    entities = [OcupacionEntity::class, EmpleadoEntity::class],
    version = 2
)

abstract class OcupacionDb: RoomDatabase() {
    abstract fun ocupacionDao(): OcupacionDao

    abstract fun empleadoDao(): EmpleadoDao
}