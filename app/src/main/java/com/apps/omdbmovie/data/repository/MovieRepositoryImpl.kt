package com.apps.omdbmovie.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apps.omdbmovie.data.local.db.OmdbDao
import com.apps.omdbmovie.data.local.model.MovieEntity
import com.apps.omdbmovie.data.network.ApiService
import com.apps.omdbmovie.data.paging.MovieRemoteMediator
import com.apps.omdbmovie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val omdbDao: OmdbDao
) : MovieRepository {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMovies(query: String): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(
                prefetchDistance = 1,
                enablePlaceholders = false,
                pageSize = 10,
            ),
            remoteMediator = MovieRemoteMediator(apiService, query, omdbDao),
            pagingSourceFactory = {
                omdbDao.getMoviesPagingSource()
            }
        ).flow
    }
}