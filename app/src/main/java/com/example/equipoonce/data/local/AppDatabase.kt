package com.example.equipoonce.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RetoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun retoDao(): RetoDao
}
