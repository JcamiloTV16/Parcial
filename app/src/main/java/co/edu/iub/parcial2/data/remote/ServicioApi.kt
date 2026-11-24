package co.edu.iub.parcial2.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface ServicioApi {
    @GET("services")
    suspend fun listarServicios(): List<ServicioDto>

    @GET("services/{id}")
    suspend fun obtenerServicio(@Path("id") id: Int): ServicioDto
}