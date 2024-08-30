package com.apps.omdbmovie.domain.usecase

import android.util.Log
import com.apps.omdbmovie.domain.repository.MovieRepository
import androidx.paging.PagingData
import androidx.paging.map
import com.apps.omdbmovie.data.local.model.MovieEntity
import kotlinx.coroutines.flow.Flow

class GetMovieUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun getMovies(query: String): Flow<PagingData<MovieEntity>> {
        Log.d("GetMovieUseCase", "getMovies: $query")
        return movieRepository.getMovies(query)
    }
}