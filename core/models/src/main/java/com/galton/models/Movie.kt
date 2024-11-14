package com.galton.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "collectionId") private val id: String?,
    private val trackName: String?,
    @Json(name = "artworkUrl100") private val artworkUrl: String?,
    @Json(name = "trackPrice") private val price: String?,
    @Json(name = "primaryGenreName") private val genre: String?,
    @Json(name = "longDescription") private val description: String?
)