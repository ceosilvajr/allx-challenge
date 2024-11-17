package com.galton.database.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.galton.database.MOVIE_TABLE

@Entity(tableName = MOVIE_TABLE)
data class MovieTable(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "price") val price: String?,
    @ColumnInfo(name = "genre") val genre: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "favorite") val favorite: Boolean?,
    @ColumnInfo(name = "artist_name") val artistName: String?
)