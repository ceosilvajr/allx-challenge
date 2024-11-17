package com.galton.movies.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.galton.models.Movie
import com.galton.movies.repository.MovieRepository
import com.galton.movies.toMovie

class MoviesPagingSource(
    private val repository: MovieRepository,
    private val favorite: Boolean
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val pageNumber = params.key ?: 1
            val data = if (favorite) {
                repository.getFavoriteMovies()
            } else {
                repository.getMovies()
            }.map {
                it.toMovie()
            }
            LoadResult.Page(
                data = data,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (data.isNotEmpty()) pageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}