package com.galton.movies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.galton.movies.repository.MovieRepository
import com.galton.network.NetworkManager
import com.galton.utils.Resource
import com.galton.utils.toError
import com.galton.utils.toLoading
import com.galton.utils.toNetworkError
import com.galton.utils.toSuccess
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class MovieViewModel(app: Application) : AndroidViewModel(app), KoinComponent {

    companion object {
        private const val PAGING_PAGE_SIZE = 50
    }

    private val repository: MovieRepository by inject()
    private val networkManager: NetworkManager by inject()
    private val _moviesNetworkCall = MutableLiveData<Resource<Boolean>>()
    val moviesNetworkCall = _moviesNetworkCall

    val moviesLiveData = repository.cachedMovies

    fun moviesPager() = Pager(
        config = PagingConfig(pageSize = PAGING_PAGE_SIZE),
        pagingSourceFactory = {
            repository.getPagingMovies()
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
            _moviesNetworkCall.toNetworkError(message = "No Internet Connection")
            return
        }
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
            Timber.e(t)
            _moviesNetworkCall.toError(message = t.message ?: "")
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            if (repository.getMovies().isEmpty()) {
                _moviesNetworkCall.toLoading()
            }
            repository.getAndUpdateMovies()
            _moviesNetworkCall.toSuccess()
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