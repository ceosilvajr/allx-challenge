package com.galton.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "collectionId") val id: Int?,
    val trackName: String?,
    @Json(name = "artworkUrl100") val artworkUrl: String?,
    @Json(name = "trackPrice") val price: String?,
    @Json(name = "primaryGenreName") val genre: String?,
    @Json(name = "longDescription") val description: String?,
    @Json(name = "favorite") val favorite: Boolean?,
    @Json(name = "artistName") val artistName: String?,
    @Json(name = "updatedAt") val updatedAt: Long?
)
