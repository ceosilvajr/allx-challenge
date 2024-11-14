package com.galton.movies

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

class MovieViewModel constructor(app: Application) : AndroidViewModel(app), KoinComponent {

    private val repository: MovieRepository by inject()
    private val networkManager: NetworkManager by inject()
    private val _moviesNetworkCall = MutableLiveData<Resource<Boolean>>()
    val moviesNetworkCall = _moviesNetworkCall

    val moviesLiveData = repository.cachedMovies

    fun getMovies() {
        if (networkManager.isNetworkConnected.not()) {
            _moviesNetworkCall.toNetworkError(message = "No Internet Connection")
            return
        }
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
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

    fun addFavorite(movieId: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
            _moviesNetworkCall.toError(message = t.message ?: "")
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.addFavoriteMovie(movieId)
        }
    }

    fun getFilteredList(searchText: String?) {
        if (searchText.isNullOrBlank()) return
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
            Log.e("", "", t)
            // TODO add error handling
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.getFilteredMovieList(searchText)
        }
    }
}