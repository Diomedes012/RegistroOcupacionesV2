package ucne.edu.registroocupacionesv2.domain.ocupaciones.usecase

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ucne.edu.registroocupacionesv2.domain.ocupaciones.model.Ocupacion
import ucne.edu.registroocupacionesv2.domain.ocupaciones.repository.OcupacionRepository

@OptIn(ExperimentalCoroutinesApi::class)
class UpsertOcupacionUseCaseTest {

    private lateinit var useCase: UpsertOcupacionUseCase
    private lateinit var repository: OcupacionRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpsertOcupacionUseCase(repository)
    }

    @Test
    fun `invoke guarda ocupacion con datos validos`() = runTest {
        // Given
        val ocupacion = Ocupacion(ocupacionId = 0, descripcion = "Desarrollador", sueldo = 60000.0)
        coEvery { repository.existsByDescripcion("Desarrollador") } returns false
        coEvery { repository.upsert(ocupacion) } returns 1

        // When
        val result = useCase(ocupacion)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
        coVerify { repository.upsert(ocupacion) }
    }

    @Test
    fun `invoke falla con descripcion vacia`() = runTest {
        // Given
        val ocupacion = Ocupacion(ocupacionId = 0, descripcion = "   ", sueldo = 60000.0)

        // When
        val result = useCase(ocupacion)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Descripcion es Obligatoria", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke falla con sueldo invalido`() = runTest {
        // Given
        val ocupacion = Ocupacion(ocupacionId = 0, descripcion = "Desarrollador", sueldo = 0.0)

        // When
        val result = useCase(ocupacion)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla cuando ocupacion ya existe`() = runTest {
        // Given
        val ocupacion = Ocupacion(ocupacionId = 0, descripcion = "Desarrollador", sueldo = 60000.0)
        coEvery { repository.existsByDescripcion("Desarrollador") } returns true

        // When
        val result = useCase(ocupacion)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Ya existe ocupacion con esta descripcion", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke propaga errores del repositorio`() = runTest {
        // Given
        val ocupacion = Ocupacion(ocupacionId = 1, descripcion = "Desarrollador", sueldo = 60000.0)
        coEvery { repository.existsByDescripcion(any()) } returns false

        coEvery { repository.upsert(ocupacion) } throws Exception("Error de base de datos")

        // When
        val result = useCase(ocupacion)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Error de base de datos", result.exceptionOrNull()?.message)
    }
}