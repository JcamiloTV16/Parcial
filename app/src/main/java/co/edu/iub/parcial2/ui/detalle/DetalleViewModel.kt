package co.edu.iub.parcial2.ui.detalle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.iub.parcial2.domain.model.Servicio
import co.edu.iub.parcial2.domain.repository.CampusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetalleUiState(
    val servicio: Servicio? = null,
    val cargando: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DetalleViewModel @Inject constructor(
    private val repository: CampusRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val serviceId: Int = savedStateHandle.get<Int>("serviceId") ?: -1

    var uiState by mutableStateOf(DetalleUiState())
        private set

    fun cargar() {
        if (serviceId == -1) {
            uiState = uiState.copy(error = "Identificador de servicio inválido.")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(cargando = true, error = null)
            try {
                val servicio = repository.obtenerServicio(serviceId)
                if (servicio == null) {
                    uiState = uiState.copy(
                        servicio = null,
                        error = "No se encontró el servicio solicitado."
                    )
                } else {
                    uiState = uiState.copy(servicio = servicio)
                }
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.message ?: "Error al cargar el detalle del servicio."
                )
            } finally {
                uiState = uiState.copy(cargando = false)
            }
        }
    }
}