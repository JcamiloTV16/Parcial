package co.edu.iub.parcial2.di

import co.edu.iub.parcial2.data.repository.CampusRepositoryImpl
import co.edu.iub.parcial2.domain.repository.CampusRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCampusRepository(
        impl: CampusRepositoryImpl
    ): CampusRepository
}