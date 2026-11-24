package co.edu.iub.parcial2.ui.misreportes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import co.edu.iub.parcial2.ui.SesionAnonima
import co.edu.iub.parcial2.ui.components.CampusError
import co.edu.iub.parcial2.ui.components.ReporteCard

@Composable
fun MisReportesRoute(
    onVolver: () -> Unit,
    viewModel: MisReportesViewModel = hiltViewModel()
) {
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.cargar()
    }

    MisReportesScreen(
        estado = viewModel.uiState,
        onReintentar = viewModel::cargar,
        onVolver = onVolver
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisReportesScreen(
    estado: MisReportesUiState,
    onReintentar: () -> Unit,
    onVolver: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Reportes") },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Historial del código anónimo: ${SesionAnonima.AUTHOR_CODE}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.fillMaxSize()) {
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

                    estado.reportes.isEmpty() -> {
                        Text(
                            text = "Aún no tienes reportes registrados.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(
                                items = estado.reportes,
                                key = { reporte -> reporte.id }
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