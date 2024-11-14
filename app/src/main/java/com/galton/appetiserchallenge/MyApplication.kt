package com.galton.appetiserchallenge

import android.app.Application
import com.galton.movies.moviesModule
import com.galton.network.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        val modules = ArrayList<Module>()
        modules.add(moviesModule)
        modules.add(networkModule)
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(modules)
        }
    }
}