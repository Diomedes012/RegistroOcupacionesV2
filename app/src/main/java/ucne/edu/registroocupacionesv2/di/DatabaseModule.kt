package ucne.edu.registroocupacionesv2.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ucne.edu.registroocupacionesv2.data.database.OcupacionDb
import ucne.edu.registroocupacionesv2.data.ocupaciones.local.OcupacionDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun ProvideOcupacionDatabase(@ApplicationContext context: Context): OcupacionDb
    {
        return Room.databaseBuilder(
            context,
            OcupacionDb::class.java,
            "Ocupacion.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideOcupacionDao(database: OcupacionDb): OcupacionDao
    {
        return database.ocupacionDao()
    }
}