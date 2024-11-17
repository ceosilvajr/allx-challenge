package com.galton.database.movie

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galton.database.MOVIE_TABLE

@Dao
interface MovieDao {

    @Query("SELECT * FROM $MOVIE_TABLE")
    suspend fun getAll(): List<MovieTable>

    @Query("SELECT * FROM $MOVIE_TABLE WHERE id = :id")
    fun getById(id: Int): MovieTable

    @Query("SELECT * FROM $MOVIE_TABLE")
    fun getAllLiveData(): LiveData<List<MovieTable>>

    @Query("SELECT * FROM $MOVIE_TABLE WHERE favorite = 1")
    fun getFavoritesLiveData(): LiveData<List<MovieTable>>

    @Query("SELECT * FROM $MOVIE_TABLE WHERE favorite = 1")
    suspend fun getFavorites(): List<MovieTable>

    @Query("SELECT * FROM $MOVIE_TABLE")
    fun pagingMovies(): PagingSource<Int, MovieTable>

    @Query(
        "SELECT * FROM $MOVIE_TABLE " +
                "WHERE `name` LIKE :searchText || '%' " +
                "ORDER by `name` DESC"
    )
    fun pagingSearchedMovies(searchText: String?): PagingSource<Int, MovieTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieTable>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movies: MovieTable)

    @Query("DELETE FROM $MOVIE_TABLE")
    suspend fun clearTable()

    @Transaction
    suspend fun clearThenInsert(movies: List<MovieTable>) {
        clearTable()
        insertAll(movies)
    }

    @Transaction
    suspend fun clearThenInsertFavorites(movies: List<MovieTable>) {
        clearTable()
        insertAll(movies)
    }

    @Transaction
    suspend fun insertFavoriteMovie(movieId: Int) {
        val movie = getById(movieId).copy(favorite = true)
        insert(movie)
    }

    @Transaction
    suspend fun deleteFavoriteMovie(movieId: Int) {
        val movie = getById(movieId).copy(favorite = false)
        insert(movie)
    }
}