package ucne.edu.registroocupacionesv2.presentation.empleado

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import ucne.edu.registroocupacionesv2.domain.empleados.model.Empleado
import ucne.edu.registroocupacionesv2.domain.empleados.usecase.DeleteEmpleadoUseCase
import ucne.edu.registroocupacionesv2.domain.empleados.usecase.ObserveEmpleadoUseCase
import ucne.edu.registroocupacionesv2.presentation.empleados.list.EmpleadoListUiEvent
import ucne.edu.registroocupacionesv2.presentation.empleados.list.EmpleadoListViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class EmpleadoListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: EmpleadoListViewModel
    private lateinit var observeEmpleadosUseCase: ObserveEmpleadoUseCase
    private lateinit var deleteEmpleadoUseCase: DeleteEmpleadoUseCase

    @Before
    fun setup() {
        observeEmpleadosUseCase = mockk()
        deleteEmpleadoUseCase = mockk()

        every { observeEmpleadosUseCase() } returns flowOf(emptyList())

        viewModel = EmpleadoListViewModel(
            observeEmpleadosUseCase,
            deleteEmpleadoUseCase
        )
    }

    @Test
    fun `loadEmpleados carga lista de empleados correctamente`() = runTest {
        // Given
        val empleados = listOf(
            Empleado(empleadoId = 1, nombres = "Victor Manuel", sexo = "Masculino", fechaIngreso = "15052026", sueldo = 100.0),
            Empleado(empleadoId = 2, nombres = "Ana Maria", sexo = "Femenino", fechaIngreso = "10022026", sueldo = 200.0)
        )
        every { observeEmpleadosUseCase() } returns flowOf(empleados)

        // When
        viewModel = EmpleadoListViewModel(observeEmpleadosUseCase, deleteEmpleadoUseCase)
        advanceUntilIdle() // Espera a que terminen las corrutinas

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(2, viewModel.state.value.empleados.size)
        assertEquals("Victor Manuel", viewModel.state.value.empleados[0].nombres)
    }

    @Test
    fun `onEvent Delete elimina empleado`() = runTest {
        // Given
        val empleadoId = 1
        coEvery { deleteEmpleadoUseCase(empleadoId) } just Runs

        // When
        viewModel.onEvent(EmpleadoListUiEvent.Delete(empleadoId))
        advanceUntilIdle()

        // Then
        coVerify { deleteEmpleadoUseCase(empleadoId) }
        assertEquals("Empleado eliminado", viewModel.state.value.message)
    }

    @Test
    fun `onEvent CreateNew activa navegacion a crear`() {
        // When
        viewModel.onEvent(EmpleadoListUiEvent.CreateNew)

        // Then
        assertTrue(viewModel.state.value.navigateToCreate)
    }

    @Test
    fun `onEvent Edit activa navegacion a editar con id`() {
        // Given
        val empleadoId = 5

        // When
        viewModel.onEvent(EmpleadoListUiEvent.Edit(empleadoId))

        // Then
        assertEquals(empleadoId, viewModel.state.value.navigateToEditId)
    }

    @Test
    fun `onEvent ClearMessage limpia mensaje`() {
        // Given
        viewModel.onEvent(EmpleadoListUiEvent.ShowMessage("Test message"))

        // When
        viewModel.onEvent(EmpleadoListUiEvent.ClearMessage)

        // Then
        assertNull(viewModel.state.value.message)
    }
}

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}