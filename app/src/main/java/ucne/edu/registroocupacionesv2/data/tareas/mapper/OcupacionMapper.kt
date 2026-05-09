package ucne.edu.registroocupacionesv2.data.tareas.mapper

import ucne.edu.registroocupacionesv2.data.tareas.local.OcupacionEntity
import ucne.edu.registroocupacionesv2.domain.tareas.model.Ocupacion

fun OcupacionEntity.toDomain(): Ocupacion = Ocupacion(
    ocupacionId = ocupacionId,
    descripcion = descripcion,
    sueldo = sueldo
)

fun Ocupacion.toEntity(): OcupacionEntity = OcupacionEntity(
    ocupacionId = ocupacionId,
    descripcion = descripcion,
    sueldo = sueldo
)