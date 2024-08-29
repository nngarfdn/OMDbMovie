package com.apps.omdbmovie.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.apps.omdbmovie.data.local.model.MovieEntity

@Dao
interface OmdbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)
}