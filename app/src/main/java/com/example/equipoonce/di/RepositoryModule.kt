package com.example.equipoonce.di

import com.example.equipoonce.data.repository.PokemonRepository
import com.example.equipoonce.data.repository.RetoRepository
import com.example.equipoonce.domain.repository.IPokemonRepository
import com.example.equipoonce.domain.repository.IRetoRepository
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
    abstract fun bindRetoRepository(impl: RetoRepository): IRetoRepository

    @Binds
    @Singleton
    abstract fun bindPokemonRepository(impl: PokemonRepository): IPokemonRepository
}
