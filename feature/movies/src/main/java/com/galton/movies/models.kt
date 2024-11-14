package com.galton.movies

import com.galton.database.movie.MovieTable
import com.galton.models.Movie

fun Movie.toMovieTable(favorite: Boolean): MovieTable {
    return MovieTable(
        id = this.id ?: "",
        name = this.trackName,
        imageUrl = this.artworkUrl,
        price = this.price,
        genre = this.genre,
        description = this.description,
        favorite = favorite,
    )
}