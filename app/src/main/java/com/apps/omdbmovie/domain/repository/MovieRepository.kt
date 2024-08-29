package com.apps.omdbmovie.domain.repository

import androidx.paging.PagingData
import com.apps.omdbmovie.data.local.model.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
   suspend fun getMovies(query: String): Flow<PagingData<MovieEntity>>
}