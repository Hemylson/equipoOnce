package com.example.equipoonce.di

import android.content.Context
import androidx.room.Room
import com.example.equipoonce.data.local.AppDatabase
import com.example.equipoonce.data.local.RetoDao
import com.example.equipoonce.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME).build()

    @Provides
    fun provideRetoDao(db: AppDatabase): RetoDao = db.retoDao()
}
