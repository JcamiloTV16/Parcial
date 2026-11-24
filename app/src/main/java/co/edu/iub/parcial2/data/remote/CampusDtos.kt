package co.edu.iub.parcial2.data.remote

import co.edu.iub.parcial2.domain.model.Reporte
import co.edu.iub.parcial2.domain.model.Servicio

data class ServicioDto(
    val id: String?,
    val nombre: String?,
    val categoria: String?,
    val descripcion: String?,
    val horario: String?,
    val ubicacion: String?,
    val disponibilidad: String?,
    val condicionesAcceso: String?,
    val fechaActualizacion: String?
) {
    fun toServicio(): Servicio = Servicio(
        id = requireNotNull(id?.toIntOrNull()) { "Identificador de servicio inválido" },
        nombre = nombre ?: "Sin nombre",
        categoria = categoria ?: "General",
        descripcion = descripcion ?: "Sin descripción",
        horario = horario ?: "No disponible",
        ubicacion = ubicacion ?: "No especificada",
        disponibilidad = disponibilidad ?: "Sin información",
        condicionesAcceso = condicionesAcceso ?: "No especificadas",
        fechaActualizacion = fechaActualizacion ?: "Sin fecha"
    )
}

data class ReporteDto(
    val id: String?,
    val serviceId: String?,
    val categoria: String?,
    val descripcion: String?,
    val estado: String?,
    val fecha: String?,
    val authorCode: String?
) {
    fun toReporte(): Reporte = Reporte(
        id = requireNotNull(id?.toIntOrNull()) { "Identificador de reporte inválido" },
        serviceId = serviceId?.toIntOrNull() ?: -1,
        categoria = categoria ?: "General",
        descripcion = descripcion ?: "",
        estado = estado ?: "RECIBIDO",
        fecha = fecha ?: "",
        authorCode = authorCode ?: ""
    )
}

// Lo que enviamos al servidor al crear un reporte (sin id, porque lo asigna el backend)
data class ReporteRequest(
    val serviceId: Int,
    val categoria: String,
    val descripcion: String,
    val authorCode: String
)