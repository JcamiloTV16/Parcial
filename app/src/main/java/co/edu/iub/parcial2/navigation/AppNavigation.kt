package co.edu.iub.parcial2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.edu.iub.parcial2.ui.catalogo.CatalogoRoute
import co.edu.iub.parcial2.ui.confirmacion.ConfirmacionScreen
import co.edu.iub.parcial2.ui.detalle.DetalleRoute
import co.edu.iub.parcial2.ui.home.HomeRoute
import co.edu.iub.parcial2.ui.misreportes.MisReportesRoute
import co.edu.iub.parcial2.ui.reporte.ReporteRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // 1. Pantalla de Inicio
        composable("home") {
            HomeRoute(
                onVerCatalogo = { navController.navigate("catalogo") },
                onVerMisReportes = { navController.navigate("mis_reportes") },
                onServicioClick = { serviceId ->
                    navController.navigate("detalle/$serviceId")
                }
            )
        }

        // 2. Pantalla de Catálogo
        composable("catalogo") {
            CatalogoRoute(
                onServicioClick = { serviceId ->
                    navController.navigate("detalle/$serviceId")
                },
                onVolver = { navController.popBackStack() }
            )
        }

        // 3. Pantalla de Detalle del Servicio
        composable(
            route = "detalle/{serviceId}",
            arguments = listOf(
                navArgument("serviceId") { type = NavType.IntType }
            )
        ) {
            DetalleRoute(
                onReportarClick = { serviceId ->
                    navController.navigate("reporte/$serviceId")
                },
                onVolver = { navController.popBackStack() }
            )
        }

        // 4. Pantalla de Formulario de Novedad
        composable(
            route = "reporte/{serviceId}",
            arguments = listOf(
                navArgument("serviceId") { type = NavType.IntType }
            )
        ) {
            ReporteRoute(
                onReporteCreado = { reporteId ->
                    navController.navigate("confirmacion/$reporteId") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onVolver = { navController.popBackStack() }
            )
        }

        // 5. Pantalla de Confirmación
        composable(
            route = "confirmacion/{reporteId}",
            arguments = listOf(
                navArgument("reporteId") { type = NavType.IntType }
            )
        ) { entrada ->
            val reporteId = entrada.arguments?.getInt("reporteId") ?: -1
            ConfirmacionScreen(
                reporteId = reporteId,
                onVerMisReportes = {
                    navController.navigate("mis_reportes") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onIrInicio = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }

        // 6. Pantalla de Mis Reportes
        composable("mis_reportes") {
            MisReportesRoute(
                onVolver = { navController.popBackStack() }
            )
        }
    }
}