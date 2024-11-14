package com.galton.movies

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.galton.models.Movie
import com.galton.utils.Resource
import com.galton.utils.toError
import com.galton.utils.toLoading
import com.galton.utils.toSuccess
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MovieViewModel constructor(app: Application) : AndroidViewModel(app), KoinComponent {

    private val repository: MovieRepository by inject()
    private val _moviesLiveData = MutableLiveData<Resource<List<Movie>>>()
    val moviesLiveData = _moviesLiveData

    fun getMovies() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
            Log.e("", "", t)
            _moviesLiveData.toError(message = t.message ?: "")
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            _moviesLiveData.toLoading()
            val movies = repository.getMovies()
            _moviesLiveData.toSuccess(movies)
        }
    }
}