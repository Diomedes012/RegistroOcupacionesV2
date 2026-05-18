package ucne.edu.registroocupacionesv2.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ucne.edu.registroocupacionesv2.data.empleados.repository.EmpleadoRepositoryImpl
import ucne.edu.registroocupacionesv2.data.ocupaciones.repository.OcupacionRepositoryImpl
import ucne.edu.registroocupacionesv2.domain.empleados.repository.EmpleadoRepository
import ucne.edu.registroocupacionesv2.domain.ocupaciones.repository.OcupacionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindOcupacionRepository(impl: OcupacionRepositoryImpl): OcupacionRepository

    @Binds
    @Singleton
    abstract fun bindEmpleadoRepository(impl: EmpleadoRepositoryImpl): EmpleadoRepository
}