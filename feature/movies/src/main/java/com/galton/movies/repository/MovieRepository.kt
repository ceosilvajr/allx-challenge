package com.galton.movies.repository

import androidx.paging.PagingSource
import com.galton.database.movie.MovieDao
import com.galton.database.movie.MovieTable
import com.galton.movies.toMovie
import com.galton.movies.toMovieTable
import com.galton.network.MoviesApiService
import kotlinx.coroutines.flow.map

class MovieRepository(val api: MoviesApiService, val movieDao: MovieDao) {

    fun getMovieById(id: Int) = movieDao.getById(id)?.map { it.toMovie() }

    fun getPagingMovies(): PagingSource<Int, MovieTable> {
        return movieDao.pagingMovies()
    }

    fun getFavoriteMovies(): PagingSource<Int, MovieTable> {
        return movieDao.pagingFavoriteMovies()
    }

    fun getSearchedMovies(searchText: String?): PagingSource<Int, MovieTable> {
        return movieDao.pagingSearchedMovies(searchText)
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
            val updatedAt = System.currentTimeMillis()
            it.toMovieTable(false, updatedAt)
        }
        retainFavoritesData(response)
    }

    suspend fun addFavoriteMovie(movieId: Int) {
        movieDao.insertFavoriteMovie(movieId)
    }

    suspend fun deleteFavoriteMovie(movieId: Int) {
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
            movieDao.clearThenInsert(response)
        } else {
            val cachedFavoriteIds = cachedFavorites.map { it.id }
            val notFavoriteMovies = response.filter { !cachedFavoriteIds.contains(it.id) }
            movieDao.clearThenInsert(cachedFavorites.plus(notFavoriteMovies))
        }
    }
}