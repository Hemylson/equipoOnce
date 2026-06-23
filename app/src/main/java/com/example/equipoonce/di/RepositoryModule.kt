package com.example.equipoonce.di

import com.example.equipoonce.repository.RetoRepository
import com.example.equipoonce.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // Mientras Alan y Emilson suben sus interfaces, proveemos las implementaciones actuales directamente
    // para evitar el crash por falta de dependencias en Hilt.
    
    /* 
    @Provides
    @Singleton
    fun provideRetoRepository(retoRepository: RetoRepository): RetoRepository = retoRepository
    */
    
    // Al usar @Inject constructor en las clases de repositorio, Hilt ya sabe cómo crearlas
    // si sus dependencias (Firestore, ApiService) están en otros módulos.
    // No necesitamos @Provides si no hay interfaces involucradas aún.
}
