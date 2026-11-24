package co.edu.iub.parcial2.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.iub.parcial2.domain.model.Reporte
import co.edu.iub.parcial2.domain.model.Servicio
import co.edu.iub.parcial2.domain.repository.CampusRepository
import co.edu.iub.parcial2.ui.SesionAnonima
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val servicios: List<Servicio> = emptyList(),
    val reportesRecientes: List<Reporte> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CampusRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    fun cargar() {
        viewModelScope.launch {
            uiState = uiState.copy(cargando = true, error = null)
            try {
                val servicios = repository.listarServicios()
                val reportes = repository.listarReportes(SesionAnonima.AUTHOR_CODE)
                uiState = uiState.copy(
                    servicios = servicios,
                    reportesRecientes = reportes.takeLast(3).reversed()
                )
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.message ?: "Error al cargar el resumen del campus."
                )
            } finally {
                uiState = uiState.copy(cargando = false)
            }
        }
    }
}