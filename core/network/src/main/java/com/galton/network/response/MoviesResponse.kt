package com.galton.network.response

import com.galton.models.Movie

data class MoviesResponse(
    val resultCount: Int?,
    val results: List<Movie>
)