package co.edu.iub.parcial2.domain.exception

open class CampusException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

class ServicioNoEncontradoException(
    message: String = "El servicio solicitado no existe."
) : CampusException(message)