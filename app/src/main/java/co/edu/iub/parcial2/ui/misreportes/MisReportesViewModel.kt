package co.edu.iub.parcial2.ui.misreportes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.iub.parcial2.domain.model.Reporte
import co.edu.iub.parcial2.domain.repository.CampusRepository
import co.edu.iub.parcial2.ui.SesionAnonima
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MisReportesUiState(
    val reportes: List<Reporte> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MisReportesViewModel @Inject constructor(
    private val repository: CampusRepository
) : ViewModel() {

    var uiState by mutableStateOf(MisReportesUiState())
        private set

    fun cargar() {
        viewModelScope.launch {
            uiState = uiState.copy(cargando = true, error = null)
            try {
                val lista = repository.listarReportes(SesionAnonima.AUTHOR_CODE)
                uiState = uiState.copy(reportes = lista.reversed())
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.message ?: "No se pudo cargar tu historial de reportes."
                )
            } finally {
                uiState = uiState.copy(cargando = false)
            }
        }
    }
}