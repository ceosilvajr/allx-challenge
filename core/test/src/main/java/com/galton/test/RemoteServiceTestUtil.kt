package com.galton.test

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

object RemoteServiceTestUtil {

    fun <T> getRetrofit(
        baseUrl: String,
        service: Class<T>,
        vararg interceptor: Interceptor
    ): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                getOkHttpClient(
                    getLoggingInterceptor(),
                    *interceptor
                )
            )
            .addConverterFactory(MoshiConverterFactory.create(buildMoshi()))
            .build()

        return retrofit.create(service)
    }

    private fun getOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        vararg interceptor: Interceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)

        interceptor.forEach {
            builder.addInterceptor(it)
        }
        builder.addInterceptor(httpLoggingInterceptor)
        return builder.build()
    }

    private fun buildMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.NONE
        return logging
    }

    fun readResourceFileUnitTest(path: String): String {
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(path))
        val content = reader.readText()
        reader.close()
        return content
    }
}
