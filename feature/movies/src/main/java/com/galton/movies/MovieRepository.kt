package com.galton.movies

import com.galton.models.Movie
import com.galton.network.MoviesApiService

class MovieRepository(val api: MoviesApiService) {

    suspend fun getMovies(): List<Movie> {
        return api.getMovies(
            term = "star",
            country = "au",
            media = "movie"
        ).results
    }
}