package com.apps.omdbmovie.domain.repository

interface MovieRepository {
   suspend fun getMovies()
}