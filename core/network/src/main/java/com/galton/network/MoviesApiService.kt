package com.galton.network

import com.galton.network.response.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {

    @GET("search")
    suspend fun getMovies(
        @Query("term") term: String,
        @Query("country") country: String,
        @Query("media") media: String,
        @Query(";all") all: String = "",
    ): MoviesResponse
}