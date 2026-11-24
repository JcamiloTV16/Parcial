package co.edu.iub.parcial2.domain.model

data class Reporte(
    val id: Int,
    val serviceId: Int,
    val categoria: String,
    val descripcion: String,
    val estado: String,
    val fecha: String,
    val authorCode: String
)