package co.edu.iub.parcial2.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReporteApi {
    @GET("reports")
    suspend fun listarReportes(@Query("authorCode") authorCode: String): List<ReporteDto>

    @POST("reports")
    suspend fun crearReporte(@Body reporte: ReporteRequest): ReporteDto
}