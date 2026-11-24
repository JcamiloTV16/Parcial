package co.edu.iub.parcial2.di

import co.edu.iub.parcial2.data.remote.ReporteApi
import co.edu.iub.parcial2.data.remote.ServicioApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://127.0.0.1:3000/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideServicioApi(retrofit: Retrofit): ServicioApi {
        return retrofit.create(ServicioApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReporteApi(retrofit: Retrofit): ReporteApi {
        return retrofit.create(ReporteApi::class.java)
    }
}