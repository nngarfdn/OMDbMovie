package com.apps.omdbmovie.data.local.db

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.apps.omdbmovie.data.local.model.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun omdbDao() : OmdbDao
    companion object {
        const val NAME = "omdb.db"
    }

}