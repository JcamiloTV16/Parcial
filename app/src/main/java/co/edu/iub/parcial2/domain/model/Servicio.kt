package co.edu.iub.parcial2.domain.model

data class Servicio(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val horario: String,
    val ubicacion: String,
    val disponibilidad: String,
    val condicionesAcceso: String,
    val fechaActualizacion: String
)
