package ucne.edu.registroocupacionesv2.data.tareas.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ocupaciones")
data class OcupacionEntity (
    @PrimaryKey(autoGenerate = true)
    val OcupacionId: Int = 0,
    val Descripcion: String,
    val Sueldo: Double
)