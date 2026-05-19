package ucne.edu.registroocupacionesv2.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ucne.edu.registroocupacionesv2.presentation.empleados.form.EmpleadoFormScreen
import ucne.edu.registroocupacionesv2.presentation.empleados.list.EmpleadoListScreen
import ucne.edu.registroocupacionesv2.presentation.ocupaciones.form.OcupacionFormScreen
import ucne.edu.registroocupacionesv2.presentation.ocupaciones.list.OcupacionlistScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.EmpleadoList,
        modifier = modifier,

        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            )
        }
    ) {
        composable<Screen.EmpleadoList> {
            EmpleadoListScreen(
                onAddEmpleado = {
                    navController.navigate(Screen.EmpleadoForm())
                },
                onEditEmpleado = { id ->
                    navController.navigate(Screen.EmpleadoForm(empleadoId = id))
                }
            )
        }

        composable<Screen.EmpleadoForm> {
            EmpleadoFormScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Screen.OcupacionList> {
            OcupacionlistScreen(
                onAddOcupacion = {
                    navController.navigate(Screen.OcupacionForm())
                },
                onEditOcupacion = { id ->
                    navController.navigate(Screen.OcupacionForm(ocupacionId = id))
                }
            )
        }

        composable<Screen.OcupacionForm> {
            OcupacionFormScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}