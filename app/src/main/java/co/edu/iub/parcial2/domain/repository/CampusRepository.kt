package co.edu.iub.parcial2.domain.repository

import co.edu.iub.parcial2.domain.model.Reporte
import co.edu.iub.parcial2.domain.model.Servicio

interface CampusRepository {
    suspend fun listarServicios(): List<Servicio>
    suspend fun obtenerServicio(id: Int): Servicio?
    suspend fun enviarReporte(
        serviceId: Int,
        categoria: String,
        descripcion: String,
        authorCode: String
    ): Reporte
    suspend fun listarReportes(authorCode: String): List<Reporte>
}