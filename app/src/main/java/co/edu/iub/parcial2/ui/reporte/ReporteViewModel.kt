package co.edu.iub.parcial2.ui.reporte

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.iub.parcial2.domain.repository.CampusRepository
import co.edu.iub.parcial2.ui.SesionAnonima
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReporteUiState(
    val serviceId: Int = -1,
    val categoria: String = "Horario desactualizado",
    val descripcion: String = "",
    val mostrarErrores: Boolean = false,
    val enviando: Boolean = false,
    val error: String? = null
) {
    val categoriasDisponibles: List<String>
        get() = listOf(
            "Horario desactualizado",
            "Equipo fuera de servicio",
            "Información de acceso incompleta",
            "Otra novedad"
        )

    val descripcionValida: Boolean
        get() = descripcion.trim().length >= 10
}

@HiltViewModel
class ReporteViewModel @Inject constructor(
    private val repository: CampusRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val serviceId: Int = savedStateHandle.get<Int>("serviceId") ?: -1

    var uiState by mutableStateOf(ReporteUiState(serviceId = serviceId))
        private set

    fun onCategoriaChange(nuevaCategoria: String) {
        uiState = uiState.copy(categoria = nuevaCategoria, error = null)
    }

    fun onDescripcionChange(nuevaDescripcion: String) {
        uiState = uiState.copy(descripcion = nuevaDescripcion, error = null)
    }

    fun enviarReporte(onReporteCreado: (Int) -> Unit) {
        if (!uiState.descripcionValida) {
            uiState = uiState.copy(mostrarErrores = true)
            return
        }

        if (serviceId == -1) {
            uiState = uiState.copy(error = "No se encontró el servicio a reportar.")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(enviando = true, error = null)
            try {
                val reporteCreado = repository.enviarReporte(
                    serviceId = serviceId,
                    categoria = uiState.categoria,
                    descripcion = uiState.descripcion.trim(),
                    authorCode = SesionAnonima.AUTHOR_CODE
                )
                onReporteCreado(reporteCreado.id)
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.message ?: "No se pudo enviar el reporte."
                )
            } finally {
                uiState = uiState.copy(enviando = false)
            }
        }
    }
}