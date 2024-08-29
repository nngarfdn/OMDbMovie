package com.apps.omdbmovie.data.local.db

import androidx.room.Database
import com.apps.omdbmovie.data.local.model.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
class AppDatabase {
}