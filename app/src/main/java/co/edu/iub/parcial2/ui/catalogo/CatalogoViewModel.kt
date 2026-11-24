package co.edu.iub.parcial2.ui.catalogo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.iub.parcial2.domain.model.Servicio
import co.edu.iub.parcial2.domain.repository.CampusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CatalogoUiState(
    val servicios: List<Servicio> = emptyList(),
    val busqueda: String = "",
    val categoriaSeleccionada: String = "Todas",
    val cargando: Boolean = false,
    val error: String? = null
) {
    val categorias: List<String>
        get() = listOf("Todas") + servicios.map { it.categoria }.distinct()

    val serviciosFiltrados: List<Servicio>
        get() = servicios.filter { servicio ->
            val coincideCategoria = categoriaSeleccionada == "Todas" ||
                    servicio.categoria == categoriaSeleccionada
            val texto = busqueda.trim()
            val coincideTexto = texto.isEmpty() ||
                    servicio.nombre.contains(texto, ignoreCase = true) ||
                    servicio.categoria.contains(texto, ignoreCase = true) ||
                    servicio.descripcion.contains(texto, ignoreCase = true)
            coincideCategoria && coincideTexto
        }
}

@HiltViewModel
class CatalogoViewModel @Inject constructor(
    private val repository: CampusRepository
) : ViewModel() {

    var uiState by mutableStateOf(CatalogoUiState())
        private set

    fun cargar() {
        viewModelScope.launch {
            uiState = uiState.copy(cargando = true, error = null)
            try {
                val lista = repository.listarServicios()
                uiState = uiState.copy(servicios = lista)
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.message ?: "No se pudo cargar el catálogo."
                )
            } finally {
                uiState = uiState.copy(cargando = false)
            }
        }
    }

    fun onBusquedaChange(nuevoTexto: String) {
        uiState = uiState.copy(busqueda = nuevoTexto)
    }

    fun onCategoriaChange(categoria: String) {
        uiState = uiState.copy(categoriaSeleccionada = categoria)
    }
}