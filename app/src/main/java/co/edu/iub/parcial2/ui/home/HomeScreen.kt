package co.edu.iub.parcial2.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import co.edu.iub.parcial2.ui.components.CampusError
import co.edu.iub.parcial2.ui.components.ReporteCard
import co.edu.iub.parcial2.ui.components.ServicioCard

@Composable
fun HomeRoute(
    onVerCatalogo: () -> Unit,
    onVerMisReportes: () -> Unit,
    onServicioClick: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.cargar()
    }

    HomeScreen(
        estado = viewModel.uiState,
        onVerCatalogo = onVerCatalogo,
        onVerMisReportes = onVerMisReportes,
        onServicioClick = onServicioClick,
        onReintentar = viewModel::cargar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    estado: HomeUiState,
    onVerCatalogo: () -> Unit,
    onVerMisReportes: () -> Unit,
    onServicioClick: (Int) -> Unit,
    onReintentar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Servicios del Campus IUB") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when {
                estado.cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                estado.error != null -> {
                    CampusError(
                        mensaje = estado.error,
                        onReintentar = onReintentar,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        item {
                            Text(
                                text = "Consulta horarios, disponibilidad y reporta novedades del campus.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = onVerCatalogo,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Ver catálogo")
                                }
                                OutlinedButton(
                                    onClick = onVerMisReportes,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Mis reportes")
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Servicios destacados (${estado.servicios.size})",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        if (estado.servicios.isEmpty()) {
                            item { Text("No hay servicios disponibles en este momento.") }
                        } else {
                            items(
                                items = estado.servicios.take(3),
                                key = { servicio -> "serv_${servicio.id}" }
                            ) { servicio ->
                                ServicioCard(
                                    servicio = servicio,
                                    onClick = { onServicioClick(servicio.id) }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Novedades recientes",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        if (estado.reportesRecientes.isEmpty()) {
                            item { Text("Aún no has enviado reportes.") }
                        } else {
                            items(
                                items = estado.reportesRecientes,
                                key = { reporte -> "rep_${reporte.id}" }
                            ) { reporte ->
                                ReporteCard(reporte = reporte)
                            }
                        }
                    }
                }
            }
        }
    }
}