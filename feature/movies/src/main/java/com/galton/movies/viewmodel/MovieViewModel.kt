package com.galton.movies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.galton.movies.repository.MovieRepository
import com.galton.network.NetworkManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class MovieViewModel(app: Application) : AndroidViewModel(app), KoinComponent {

    companion object {
        private const val PAGING_PAGE_SIZE = 50
        private const val MESSAGE_NO_INTERNET = "No Internet Connection"
    }

    private val repository: MovieRepository by inject()
    private val networkManager: NetworkManager by inject()

    fun getMovieById(id: Int) = repository.getMovieById(id)

    fun moviesPager() = Pager(
        config = PagingConfig(pageSize = PAGING_PAGE_SIZE),
        pagingSourceFactory = {
            repository.getPagingMovies()
        }
    ).flow.cachedIn(viewModelScope)

    fun favoriteMoviesPager() = Pager(
        config = PagingConfig(pageSize = PAGING_PAGE_SIZE),
        pagingSourceFactory = {
            repository.getFavoriteMovies()
        }
    ).flow.cachedIn(viewModelScope)

    fun moviesPager(searchText: String?) = Pager(
        config = PagingConfig(pageSize = PAGING_PAGE_SIZE),

        pagingSourceFactory = {
            repository.getSearchedMovies(searchText)
        }
    ).flow.cachedIn(viewModelScope)

    fun getMovies() {
        if (networkManager.isNetworkConnected.not()) {
            Timber.e(MESSAGE_NO_INTERNET)
            return
        }
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
            Timber.e(t)
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.getAndUpdateMovies()
        }
    }

    fun addFavorite(movieId: Int) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
            Timber.e(t)
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.addFavoriteMovie(movieId)
        }
    }

    fun deleteFavorite(movieId: Int) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
            Timber.e(t)
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.deleteFavoriteMovie(movieId)
        }
    }
}