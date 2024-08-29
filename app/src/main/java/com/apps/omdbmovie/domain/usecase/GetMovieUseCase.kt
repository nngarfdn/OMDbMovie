package com.apps.omdbmovie.domain.usecase

import com.apps.omdbmovie.domain.repository.MovieRepository

class GetMovieUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun getMovies() {
        movieRepository.getMovies()
    }
}