package com.example.equipoonce.di

import android.content.Context
import com.example.equipoonce.data.local.AppDatabase
import com.example.equipoonce.data.local.RetoDao
import com.example.equipoonce.data.remote.PokemonApiService
import com.example.equipoonce.data.repository.PokemonRepository
import com.example.equipoonce.data.repository.RetoRepository
import com.example.equipoonce.utils.GameAudioManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideRetoDao(database: AppDatabase): RetoDao = database.retoDao()

    @Provides
    @Singleton
    fun provideRetoRepository(dao: RetoDao): RetoRepository = RetoRepository(dao)

    @Provides
    @Singleton
    fun providePokemonRepository(apiService: PokemonApiService): PokemonRepository =
        PokemonRepository(apiService)

    @Provides
    @Singleton
    fun provideGameAudioManager(@ApplicationContext context: Context): GameAudioManager =
        GameAudioManager(context)
}
