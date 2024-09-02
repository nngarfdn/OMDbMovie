package com.apps.omdbmovie.domain.usecase

import androidx.paging.PagingData
import com.apps.omdbmovie.data.local.model.MovieEntity
import com.apps.omdbmovie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMovieUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun getMovies(query: String): Flow<PagingData<MovieEntity>> {
        return movieRepository.getMovies(query)
    }
}