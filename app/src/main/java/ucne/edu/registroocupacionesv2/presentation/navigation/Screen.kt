package ucne.edu.registroocupacionesv2.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object OcupacionList : Screen()

    @Serializable
    data class OcupacionForm(val ocupacionId: Int = 0): Screen()
}