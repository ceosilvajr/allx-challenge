package com.galton.appetiserchallenge

import android.app.Application
import com.galton.database.databaseModule
import com.galton.movies.moviesModule
import com.galton.network.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import timber.log.Timber

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG && Timber.treeCount == 0) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initKoin() {
        val modules = ArrayList<Module>()
        modules.add(moviesModule)
        modules.add(networkModule)
        modules.add(databaseModule)

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(modules)
        }
    }
}
