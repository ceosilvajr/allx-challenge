package com.galton.network.response

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class MoviesResponse(
    val resultCount: Int?,
    val results: List<MovieItem>
)

@Keep
@JsonClass(generateAdapter = true)
data class MovieItem(
    @Json(name = "trackId") val id: Int?,
    val trackName: String?,
    @Json(name = "artworkUrl100") val artworkUrl: String?,
    @Json(name = "trackPrice") val price: String?,
    @Json(name = "primaryGenreName") val genre: String?,
    @Json(name = "longDescription") val description: String?,
    @Json(name = "artistName") val artistName: String?
)