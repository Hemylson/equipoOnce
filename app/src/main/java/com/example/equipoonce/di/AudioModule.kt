package com.example.equipoonce.di

import android.content.Context
import com.example.equipoonce.utils.GameAudioManager
import com.example.equipoonce.utils.IAudioManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioModule {

    @Provides
    @Singleton
    fun provideAudioManager(@ApplicationContext context: Context): IAudioManager =
        GameAudioManager(context)
}
