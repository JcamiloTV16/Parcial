package co.edu.iub.parcial2.data.repository

import co.edu.iub.parcial2.data.remote.ReporteApi
import co.edu.iub.parcial2.data.remote.ReporteRequest
import co.edu.iub.parcial2.data.remote.ServicioApi
import co.edu.iub.parcial2.domain.exception.CampusException
import co.edu.iub.parcial2.domain.exception.ServicioNoEncontradoException
import co.edu.iub.parcial2.domain.model.Reporte
import co.edu.iub.parcial2.domain.model.Servicio
import co.edu.iub.parcial2.domain.repository.CampusRepository
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CampusRepositoryImpl @Inject constructor(
    private val servicioApi: ServicioApi,
    private val reporteApi: ReporteApi
) : CampusRepository {

    override suspend fun listarServicios(): List<Servicio> = solicitar {
        servicioApi.listarServicios().map { it.toServicio() }
    }

    override suspend fun obtenerServicio(id: Int): Servicio? = try {
        solicitar { servicioApi.obtenerServicio(id).toServicio() }
    } catch (_: ServicioNoEncontradoException) {
        null
    }

    override suspend fun listarReportes(authorCode: String): List<Reporte> = solicitar {
        reporteApi.listarReportes(authorCode).map { it.toReporte() }
    }

    override suspend fun enviarReporte(
        serviceId: Int,
        categoria: String,
        descripcion: String,
        authorCode: String
    ): Reporte = solicitar {
        val request = ReporteRequest(
            serviceId = serviceId,
            categoria = categoria,
            descripcion = descripcion,
            authorCode = authorCode
        )
        reporteApi.crearReporte(request).toReporte()
    }

    private suspend fun <T> solicitar(bloque: suspend () -> T): T {
        return try {
            bloque()
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (e: CampusException) {
            throw e
        } catch (e: HttpException) {
            if (e.code() == 404) {
                throw ServicioNoEncontradoException("Recurso no encontrado.")
            } else {
                throw CampusException("Error del servidor (HTTP ${e.code()}).", e)
            }
        } catch (e: IOException) {
            throw CampusException("No se pudo conectar con el servidor. Verifica tu conexión.", e)
        } catch (e: Exception) {
            throw CampusException("Error inesperado al procesar los datos.", e)
        }
    }
}