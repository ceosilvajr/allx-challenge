package com.galton.movies

import com.galton.movies.repository.MovieRepository
import com.galton.movies.viewmodel.MovieViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val moviesModule = module {

    single {
        MovieRepository(api = get(), movieDao = get())
    }

    viewModelOf(::MovieViewModel)
}