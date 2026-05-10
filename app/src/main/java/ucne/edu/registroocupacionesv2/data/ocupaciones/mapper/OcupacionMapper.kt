package ucne.edu.registroocupacionesv2.data.ocupaciones.mapper

import ucne.edu.registroocupacionesv2.data.ocupaciones.local.OcupacionEntity
import ucne.edu.registroocupacionesv2.domain.ocupaciones.model.Ocupacion

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