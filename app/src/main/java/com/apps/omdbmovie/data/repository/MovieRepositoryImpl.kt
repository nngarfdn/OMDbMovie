package com.apps.omdbmovie.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apps.omdbmovie.data.local.db.OmdbDao
import com.apps.omdbmovie.data.local.model.MovieEntity
import com.apps.omdbmovie.data.network.ApiService
import com.apps.omdbmovie.data.paging.MoviePagingSource
import com.apps.omdbmovie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val omdbDao: OmdbDao
): MovieRepository {
    override suspend fun getMovies(query:String): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, // Number of items to load per page
                enablePlaceholders = false // Whether to enable placeholders for unloaded items
            ),
            pagingSourceFactory = { MoviePagingSource(apiService, query, omdbDao) }
        ).flow
    }
}