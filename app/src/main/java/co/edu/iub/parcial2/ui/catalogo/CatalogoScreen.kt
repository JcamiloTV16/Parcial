package co.edu.iub.parcial2.ui.catalogo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
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
import co.edu.iub.parcial2.ui.components.ServicioCard

@Composable
fun CatalogoRoute(
    onServicioClick: (Int) -> Unit,
    onVolver: () -> Unit,
    viewModel: CatalogoViewModel = hiltViewModel()
) {
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.cargar()
    }

    CatalogoScreen(
        estado = viewModel.uiState,
        onBusquedaChange = viewModel::onBusquedaChange,
        onCategoriaChange = viewModel::onCategoriaChange,
        onServicioClick = onServicioClick,
        onReintentar = viewModel::cargar,
        onVolver = onVolver
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    estado: CatalogoUiState,
    onBusquedaChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onServicioClick: (Int) -> Unit,
    onReintentar: () -> Unit,
    onVolver: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Servicios") },
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
            OutlinedTextField(
                value = estado.busqueda,
                onValueChange = onBusquedaChange,
                label = { Text("Buscar servicio o palabra clave") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(estado.categorias) { categoria ->
                    FilterChip(
                        selected = estado.categoriaSeleccionada == categoria,
                        onClick = { onCategoriaChange(categoria) },
                        label = { Text(categoria) }
                    )
                }
            }

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

                    estado.serviciosFiltrados.isEmpty() -> {
                        Text(
                            text = "No se encontraron servicios con ese filtro.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(
                                items = estado.serviciosFiltrados,
                                key = { servicio -> servicio.id }
                            ) { servicio ->
                                ServicioCard(
                                    servicio = servicio,
                                    onClick = { onServicioClick(servicio.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}