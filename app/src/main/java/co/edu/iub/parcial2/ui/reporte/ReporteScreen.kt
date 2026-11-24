package co.edu.iub.parcial2.ui.reporte

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import co.edu.iub.parcial2.ui.SesionAnonima

@Composable
fun ReporteRoute(
    onReporteCreado: (Int) -> Unit,
    onVolver: () -> Unit,
    viewModel: ReporteViewModel = hiltViewModel()
) {
    ReporteScreen(
        estado = viewModel.uiState,
        onCategoriaChange = viewModel::onCategoriaChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onEnviarClick = { viewModel.enviarReporte(onReporteCreado) },
        onVolver = onVolver
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReporteScreen(
    estado: ReporteUiState,
    onCategoriaChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onEnviarClick: () -> Unit,
    onVolver: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportar Novedad") },
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Servicio #${estado.serviceId} · Envío anónimo (${SesionAnonima.AUTHOR_CODE})",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "No incluyas nombres, teléfonos ni datos personales en el reporte.",
                style = MaterialTheme.typography.bodySmall
            )

            Text(text = "Categoría de la novedad", style = MaterialTheme.typography.titleSmall)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(estado.categoriasDisponibles) { categoria ->
                    FilterChip(
                        selected = estado.categoria == categoria,
                        onClick = { onCategoriaChange(categoria) },
                        label = { Text(categoria) }
                    )
                }
            }

            OutlinedTextField(
                value = estado.descripcion,
                onValueChange = onDescripcionChange,
                label = { Text("Describe la novedad encontrada") },
                isError = estado.mostrarErrores && !estado.descripcionValida,
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            if (estado.mostrarErrores && !estado.descripcionValida) {
                Text(
                    text = "Escribe una descripción de al menos 10 caracteres.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (estado.error != null) {
                Text(
                    text = estado.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onEnviarClick,
                enabled = !estado.enviando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (estado.enviando) {
                    CircularProgressIndicator()
                } else {
                    Text("Enviar reporte")
                }
            }
        }
    }
}