package com.galton.database

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "csj-database"
        ).build()
    }
    single {
        get<AppDatabase>().movieDao()
    }
}