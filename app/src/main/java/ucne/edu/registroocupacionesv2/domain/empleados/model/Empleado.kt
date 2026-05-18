package ucne.edu.registroocupacionesv2.domain.empleados.model

data class Empleado(
    val empleadoId: Int = 0,
    val fechaIngreso: String = "",
    val nombres: String = "",
    val sexo: String = "",
    val sueldo: Double = 0.0
)