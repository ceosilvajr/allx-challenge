package com.galton.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.galton.database.movie.MovieDao
import com.galton.database.movie.MovieTable

@Database(entities = [MovieTable::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
