package ucne.edu.registroocupacionesv2.domain.empleado.usecase

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ucne.edu.registroocupacionesv2.domain.empleados.model.Empleado
import ucne.edu.registroocupacionesv2.domain.empleados.repository.EmpleadoRepository
import ucne.edu.registroocupacionesv2.domain.empleados.usecase.UpsertEmpleadoUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class UpsertEmpleadoUseCaseTest {

    private lateinit var useCase: UpsertEmpleadoUseCase
    private lateinit var repository: EmpleadoRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpsertEmpleadoUseCase(repository)
    }

    @Test
    fun `invoke guarda empleado con datos validos`() = runTest {
        // Given
        val empleado = Empleado(0, "15052026", "Samil de la Cruz", "Masculino", 45000.0)
        coEvery { repository.upsert(empleado) } returns 1

        // When
        val result = useCase(empleado)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
        coVerify { repository.upsert(empleado) }
    }

    @Test
    fun `invoke falla con nombres vacios`() = runTest {
        // Given
        val empleado = Empleado(0, "15/05/2026", "", "Masculino", 45000.0)

        // When
        val result = useCase(empleado)

        // Then
        assertTrue(result.isFailure)
        assertEquals("El nombre es obligatorio", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke falla con fecha en blanco`() = runTest {
        // Given
        val empleado = Empleado(0, "", "Pedro", "Masculino", 45000.0)

        // When
        val result = useCase(empleado)

        // Then
        assertTrue(result.isFailure)
        assertEquals("La fecha de ingreso es obligatoria", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke falla con formato de fecha invalido`() = runTest {
        // Given
        val empleado = Empleado(0, "32/05/2026", "Pedro", "Masculino", 45000.0)

        // When
        val result = useCase(empleado)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Formato de fecha inválido. Use dd/MM/yyyy", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke propaga errores del repositorio`() = runTest {
        // Given
        val empleado = Empleado(1, "10012026", "Maria", "Femenino", 50000.0)
        coEvery { repository.upsert(empleado) } throws Exception("Fallo de conexión SQLite")

        // When
        val result = useCase(empleado)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Fallo de conexión SQLite", result.exceptionOrNull()?.message)
    }
}