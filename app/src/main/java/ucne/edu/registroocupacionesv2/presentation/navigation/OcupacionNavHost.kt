package ucne.edu.registroocupacionesv2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ucne.edu.registroocupacionesv2.presentation.ocupaciones.form.OcupacionFormScreen
import ucne.edu.registroocupacionesv2.presentation.ocupaciones.list.OcupacionlistScreen

@Composable
fun OcupacionNavHost(
    navController: NavHostController = rememberNavController()
)
{
    NavHost(
        navController =  navController,
        startDestination = Screen.OcupacionList
    )
    {
        composable<Screen.OcupacionList>{
            OcupacionlistScreen(
                onAddOcupacion = {
                    navController.navigate(Screen.OcupacionForm())
                },
                onEditOcupacion = {id ->
                    navController.navigate(Screen.OcupacionForm(ocupacionId = id))
                }
            )
        }

        composable<Screen.OcupacionForm>{
            OcupacionFormScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}