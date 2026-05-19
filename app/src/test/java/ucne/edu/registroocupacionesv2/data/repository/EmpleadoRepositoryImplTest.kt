package ucne.edu.registroocupacionesv2.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ucne.edu.registroocupacionesv2.data.empleados.local.EmpleadoDao
import ucne.edu.registroocupacionesv2.data.empleados.local.EmpleadoEntity
import ucne.edu.registroocupacionesv2.data.empleados.repository.EmpleadoRepositoryImpl
import ucne.edu.registroocupacionesv2.domain.empleados.model.Empleado


@OptIn(ExperimentalCoroutinesApi::class)
class EmpleadoRepositoryImplTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: EmpleadoRepositoryImpl
    private lateinit var dao: EmpleadoDao

    @Before
    fun setup()
    {
        dao = mockk(relaxed = true)
        repository = EmpleadoRepositoryImpl(dao)
    }

    @Test
    fun `upsert guarda empleado correctamente`() = runTest {
        // Given
        val empleado = Empleado(
            empleadoId = 0,
            nombres = "Samil de la Cruz",
            sexo = "Masculino",
            fechaIngreso = "15/05/2026",
            sueldo = 48000.0
        )
        val empleadoSlot = slot<EmpleadoEntity>()
        coEvery { dao.upsert(capture(empleadoSlot)) } just Runs

        // When
        val result = repository.upsert(empleado)

        // Then
        assertEquals(0, result)
        coVerify { dao.upsert(any()) }
        assertEquals("Samil de la Cruz", empleadoSlot.captured.nombres)
        assertEquals("15/05/2026", empleadoSlot.captured.fechaIngreso)
    }

    @Test
    fun `upsert actualiza empleado correctamente`() = runTest {
        // Given
        val empleado = Empleado(
            empleadoId = 5,
            nombres = "Empleado Actualizado",
            sexo = "Femenino",
            fechaIngreso = "01/01/2025",
            sueldo = 55000.0
        )
        coEvery { dao.upsert(any()) } just Runs

        // When
        val result = repository.upsert(empleado)

        // Then
        assertEquals(5, result)
        coVerify { dao.upsert(any()) }
    }

    @Test
    fun `delete elimina empleado correctamente`() = runTest {
        // Given
        val empleadoId = 3
        coEvery { dao.deleteById(empleadoId) } just Runs

        // When
        repository.delete(empleadoId)

        // Then
        coVerify { dao.deleteById(empleadoId) }
    }

    @Test
    fun `observeEmpleados retorna flow de empleados`() = runTest {
        // Given
        val listaMock = listOf(
            EmpleadoEntity(1, "10022026", "Ana Martinez", "Femenino", 35000.0),
            EmpleadoEntity(2, "12032026", "Luis Perez", "Masculino", 42000.0)
        )
        every { dao.observeAll() } returns  flowOf(listaMock)

        // When
        val result = repository.observeEmpleados().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Ana Martinez", result[0].nombres)
        assertEquals("Femenino", result[0].sexo)
        assertEquals("Luis Perez", result[1].nombres)
    }

    @Test
    fun `getEmpleado retorna empleado por id`() = runTest {
        // Given
        val entidad = EmpleadoEntity(10, "01/05/2026", "Carlos Reyes", "Masculino", 50000.0)
        coEvery { dao.getById(10) } returns entidad

        // When
        val result = repository.getEmpleado(10)

        // Then
        assertNotNull(result)
        assertEquals("Carlos Reyes", result?.nombres)
        assertEquals(50000.0, result?.sueldo)
    }
}