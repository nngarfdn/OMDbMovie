package com.apps.omdbmovie.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apps.omdbmovie.data.local.model.MovieEntity

@Dao
interface OmdbDao {
    @Query("SELECT * FROM movies")
    suspend fun getMovies(): List<MovieEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
}