package co.edu.iub.parcial2.ui.confirmacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import co.edu.iub.parcial2.ui.SesionAnonima

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmacionScreen(
    reporteId: Int,
    onVerMisReportes: () -> Unit,
    onIrInicio: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Reporte Enviado") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "¡Novedad registrada!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Tu reporte fue recibido por el servidor y quedó en revisión.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Número de radicado: #$reporteId",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Código anónimo: ${SesionAnonima.AUTHOR_CODE}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onVerMisReportes,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver en Mis reportes")
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onIrInicio,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al inicio")
            }
        }
    }
}