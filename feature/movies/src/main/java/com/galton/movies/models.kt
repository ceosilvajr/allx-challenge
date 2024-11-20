package com.galton.movies

import com.galton.database.movie.MovieTable
import com.galton.models.Movie
import com.galton.network.response.MovieItem

fun MovieItem.toMovieTable(favorite: Boolean, updatedAt: Long): MovieTable {
    return MovieTable(
        id = this.id ?: 0,
        name = this.trackName,
        imageUrl = this.artworkUrl,
        price = this.price,
        genre = this.genre,
        description = this.description,
        favorite = favorite,
        artistName = this.artistName,
        updatedAt = updatedAt,
    )
}

fun MovieTable.toMovie(): Movie {
    return Movie(
        id = this.id,
        trackName = this.name,
        artworkUrl = this.imageUrl,
        price = this.price,
        genre = this.genre,
        description = this.description,
        favorite = this.favorite,
        artistName = this.artistName,
        updatedAt = this.updatedAt,
    )
}

fun movieInitialState(): Movie {
    return Movie(
        id = 0,
        trackName = "",
        artworkUrl = "",
        price = "",
        genre = "",
        description = "",
        favorite = false,
        artistName = "",
        updatedAt = 0L
    )
}