package com.galton.movies

import android.util.Log
import com.galton.database.movie.MovieDao
import com.galton.database.movie.MovieTable
import com.galton.network.MoviesApiService

class MovieRepository(val api: MoviesApiService, val movieDao: MovieDao) {

    val cachedMovies = movieDao.getAllLiveData()

    val cachedFavoriteMovies = movieDao.getFavoritesLiveData()

    suspend fun getMovies(): List<MovieTable> {
        return movieDao.getAll()
    }

    suspend fun getFilteredMovieList(searchText: String): List<MovieTable> {
        return movieDao.getFilteredList(searchText)
    }

    /**
     * Retrieves and updates the list of movies.
     *
     * This function fetches movie data from the API, maps it to
     * `MovieTable` objects, and then updates the database with the new data.
     * It ensures that existing favorites are preserved during the update process.
     */
    suspend fun getAndUpdateMovies() {
        val response = api.getMovies(
            term = "star", country = "au", media = "movie"
        ).results.map {
            it.toMovieTable(false)
        }
        retainFavoritesData(response)
    }

    suspend fun getFilteredFavoriteMovieList(searchText: String): List<MovieTable> {
        return movieDao.getFilteredFavoriteList(searchText, true)
    }

    suspend fun getFavoriteMovies(searchText: String): List<MovieTable> {
        return getFilteredMovieList(searchText).filter { it.favorite == true }
    }

    suspend fun addFavoriteMovie(movieId: String) {
        movieDao.insertFavoriteMovie(movieId)
    }

    suspend fun deleteFavoriteMovie(movieId: String) {
        movieDao.deleteFavoriteMovie(movieId)
    }

    /**
     * Retains the favorite movie data from the provided response list.
     *
     * This function compares the incoming movie data with the existing favorites
     * stored in the database. If the database has no favorites, it inserts
     * all movies from the response. Otherwise, it only inserts movies from the
     * response that are not already marked as favorites in the database.
     * This ensures that existing favorites are preserved when updating the movie list.
     *
     * @param response The list of [MovieTable] objects representing the movie data.
     */
    private suspend fun retainFavoritesData(response: List<MovieTable>) {
        val cachedFavorites = movieDao.getFavorites()
        if (cachedFavorites.isEmpty()) {
            Log.d("MovieRepository", "cachedFavorites - empty")
            movieDao.clearThenInsert(response)
        } else {
            val cachedFavoriteIds = cachedFavorites.map { it.id }
            Log.d("MovieRepository", "cachedFavorites - $cachedFavoriteIds")
            movieDao.clearThenInsert(
                response.filter { !cachedFavoriteIds.contains(it.id) }
            )
        }
    }
}