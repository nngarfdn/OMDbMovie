package com.apps.omdbmovie.data.repository

import android.util.Log
import androidx.paging.*
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
    private val TAG = "MovieRepositoryImpl"

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMovies(query: String): Flow<PagingData<MovieEntity>> {
        Log.d(TAG, "getMovies: $query")
        return Pager(
            config = PagingConfig(
                prefetchDistance = 1,
                enablePlaceholders = false,
                pageSize = 10, // Jumlah item yang dimuat per halaman
            ),
            remoteMediator = MovieRemoteMediator(apiService, query, omdbDao),
            pagingSourceFactory = {
                Log.d(TAG, "Creating PagingSource")
                omdbDao.getMoviesPagingSource()
            }
        ).flow
    }
}