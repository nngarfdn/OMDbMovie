package com.apps.omdbmovie.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity (
    @PrimaryKey
    val imdbID: String = "",
    val type: String = "",
    val poster: String = "",
    val title: String = "",
    val year: String = "",
)