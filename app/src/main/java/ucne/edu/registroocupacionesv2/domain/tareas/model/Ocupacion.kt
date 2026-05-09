package ucne.edu.registroocupacionesv2.domain.tareas.model

data class Ocupacion (
    val ocupacionId: Int = 0,
    val descripcion: String,
    val sueldo: Double
)