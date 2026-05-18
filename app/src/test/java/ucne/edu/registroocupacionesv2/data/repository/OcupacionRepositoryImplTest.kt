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
import ucne.edu.registroocupacionesv2.data.ocupaciones.local.OcupacionDao
import ucne.edu.registroocupacionesv2.data.ocupaciones.local.OcupacionEntity
import ucne.edu.registroocupacionesv2.data.ocupaciones.repository.OcupacionRepositoryImpl
import ucne.edu.registroocupacionesv2.domain.ocupaciones.model.Ocupacion


@OptIn(ExperimentalCoroutinesApi::class)
class OcupacionRepositoryImplTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: OcupacionRepositoryImpl
    private lateinit var dao: OcupacionDao

    @Before
    fun setup()
    {
        dao = mockk(relaxed = true)
        repository = OcupacionRepositoryImpl(dao)
    }

    @Test
    fun `upsert guarda ocupacion correctamente`() = runTest {
        //Given
        val ocupacion = Ocupacion(
            ocupacionId = 0,
            descripcion = "Ingeniero de Software",
            sueldo = 55000.0
        )

        val ocupacionSlot = slot<OcupacionEntity>()
        coEvery { dao.upsert(capture(ocupacionSlot)) } just Runs

        //When
        val result = repository.upsert(ocupacion)

        //Then
        assertEquals(0, result)
        coVerify { dao.upsert(any()) }
    }

     @Test
     fun `delete elimina ocupacion correctamente`() = runTest {
         //Given
         val ocupacionId = 1
         coEvery { dao.deleteById(ocupacionId) } just Runs

         //When
         repository.delete(ocupacionId)

         //Then
         coVerify { dao.deleteById(ocupacionId) }
     }

    @Test
    fun `observeOcupaciones retorna flow de ocupaciones`() = runTest {
        //Given
        val entities = listOf(
            OcupacionEntity(1, "Maestro", 25000.0),
            OcupacionEntity(2, "Doctor", 60000.0)
        )
        every { dao.observeAll() } returns flowOf(entities)

        //When
        val result = repository.observeOcupaciones().first()

        //Then
        assertEquals(2, result.size)
        assertEquals("Maestro", result[0].descripcion)
        assertEquals("Doctor", result[1].descripcion)
    }

    @Test
    fun `getOcupacion retorna ocupacion por id`() = runTest {
        //Given
        val entidad = OcupacionEntity(1, "Abogado", 45000.0)

        //When
        val result = repository.getOCupacion(1)

        //Then
        assertNotNull(result)
        assertEquals("Abogado", result?.descripcion)
        assertEquals(45000.0, result?.sueldo)
    }
}