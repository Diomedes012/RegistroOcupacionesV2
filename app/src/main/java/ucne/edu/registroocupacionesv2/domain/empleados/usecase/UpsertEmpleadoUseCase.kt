package ucne.edu.registroocupacionesv2.domain.empleados.usecase

import ucne.edu.registroocupacionesv2.domain.empleados.model.Empleado
import ucne.edu.registroocupacionesv2.domain.empleados.repository.EmpleadoRepository
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.inject.Inject

class UpsertEmpleadoUseCase @Inject constructor(private val repository: EmpleadoRepository)
{
    suspend operator fun invoke(empleado: Empleado): Result<Int>
    {

        if (empleado.nombres.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre es obligatorio"))
        }

        if (empleado.sueldo <= 0.0) {
            return Result.failure(IllegalArgumentException("El sueldo debe ser mayor a cero"))
        }

        if (empleado.fechaIngreso.isBlank()) {
            return Result.failure(IllegalArgumentException("La fecha de ingreso es obligatoria"))
        }

        if (!esFechaValida(empleado.fechaIngreso)) {
            return Result.failure(IllegalArgumentException("Formato de fecha inválido. Use dd/MM/yyyy"))
        }

        return runCatching { repository.upsert(empleado) }
    }

    private fun esFechaValida(fecha: String): Boolean {
        val formato = SimpleDateFormat("ddMMyyyy", java.util.Locale.getDefault())

        formato.isLenient = false

        return try {
            formato.parse(fecha)
            true
        } catch (e: ParseException) {
            false
        }
    }
}