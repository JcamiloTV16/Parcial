package co.edu.iub.parcial2.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import co.edu.iub.parcial2.ui.components.CampusError

@Composable
fun DetalleRoute(
    onReportarClick: (Int) -> Unit,
    onVolver: () -> Unit,
    viewModel: DetalleViewModel = hiltViewModel()
) {
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.cargar()
    }

    DetalleScreen(
        estado = viewModel.uiState,
        onReportarClick = onReportarClick,
        onReintentar = viewModel::cargar,
        onVolver = onVolver
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    estado: DetalleUiState,
    onReportarClick: (Int) -> Unit,
    onReintentar: () -> Unit,
    onVolver: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Servicio") },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("Atrás")
                    }
                }
            )
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

                estado.servicio != null -> {
                    val servicio = estado.servicio
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = servicio.nombre, style = MaterialTheme.typography.headlineSmall)
                        Text(
                            text = "${servicio.categoria} · ${servicio.disponibilidad}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = servicio.descripcion, style = MaterialTheme.typography.bodyLarge)

                        HorizontalDivider()

                        Text(text = "Horario:", style = MaterialTheme.typography.titleSmall)
                        Text(text = servicio.horario, style = MaterialTheme.typography.bodyMedium)

                        Text(text = "Ubicación:", style = MaterialTheme.typography.titleSmall)
                        Text(text = servicio.ubicacion, style = MaterialTheme.typography.bodyMedium)

                        Text(text = "Condiciones de acceso:", style = MaterialTheme.typography.titleSmall)
                        Text(text = servicio.condicionesAcceso, style = MaterialTheme.typography.bodyMedium)

                        Text(
                            text = "Última actualización: ${servicio.fechaActualizacion}",
                            style = MaterialTheme.typography.labelSmall
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { onReportarClick(servicio.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Reportar novedad")
                        }
                    }
                }
            }
        }
    }
}