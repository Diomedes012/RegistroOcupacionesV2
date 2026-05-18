package ucne.edu.registroocupacionesv2.data.empleados.mapper

import ucne.edu.registroocupacionesv2.data.empleados.local.EmpleadoEntity
import ucne.edu.registroocupacionesv2.domain.empleados.model.Empleado


fun EmpleadoEntity.toDomain(): Empleado = Empleado(
    empleadoId = empleadoId,
    fechaIngreso = fechaIngreso,
    nombres = nombres,
    sexo = sexo,
    sueldo = sueldo
)


fun Empleado.toEntity(): EmpleadoEntity = EmpleadoEntity(
    empleadoId = empleadoId,
    fechaIngreso = fechaIngreso,
    nombres = nombres,
    sexo = sexo,
    sueldo = sueldo
)
