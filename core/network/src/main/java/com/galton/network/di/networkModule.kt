package com.galton.network.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.galton.network.service.MoviesApiService
import com.galton.network.NetworkManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://itunes.apple.com/"

val networkModule = module {
    single(named("networkScope")) {
        CoroutineScope(Dispatchers.Default + SupervisorJob())
    }
    single {
        NetworkManager(androidContext(), get(named("networkScope")))
    }
    single {
        HttpLoggingInterceptor()
    }
    single {
        ChuckerInterceptor
            .Builder(androidContext())
            .alwaysReadResponseBody(true)
            .build()
    }
    single {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<ChuckerInterceptor>())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(MoshiConverterFactory.create(get<Moshi>()))
            .build()
    }
    factory {
        get<Retrofit>().create(MoviesApiService::class.java)
    }
}
