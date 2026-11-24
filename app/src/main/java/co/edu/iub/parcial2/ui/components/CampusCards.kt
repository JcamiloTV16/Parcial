package co.edu.iub.parcial2.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.edu.iub.parcial2.domain.model.Reporte
import co.edu.iub.parcial2.domain.model.Servicio

@Composable
fun ServicioCard(
    servicio: Servicio,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = servicio.nombre, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${servicio.categoria} · ${servicio.disponibilidad}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = servicio.ubicacion, style = MaterialTheme.typography.bodySmall)
            Text(text = servicio.horario, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ReporteCard(
    reporte: Reporte
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Reporte #${reporte.id} · ${reporte.estado}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = reporte.categoria, style = MaterialTheme.typography.bodyMedium)
            Text(text = reporte.descripcion, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fecha: ${reporte.fecha} · Servicio #${reporte.serviceId}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}