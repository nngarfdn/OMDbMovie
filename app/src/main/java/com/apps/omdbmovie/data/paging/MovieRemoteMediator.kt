package com.apps.omdbmovie.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.apps.omdbmovie.data.local.db.OmdbDao
import com.apps.omdbmovie.data.local.model.MovieEntity
import com.apps.omdbmovie.data.mapper.MovieDataMapper
import com.apps.omdbmovie.data.network.ApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val apiService: ApiService,
    private val query: String,
    private val omdbDao: OmdbDao
) : RemoteMediator<Int, MovieEntity>() {
    private val TAG = "MovieRemoteMediator"
    private var currentPage = 1

    init {
        Log.d(TAG, "MovieRemoteMediator initialized with query: $query")
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        Log.d(TAG, "load called with loadType: $loadType")

        // Determine the page number
        val page = when (loadType) {
            LoadType.REFRESH -> {
                currentPage = 1 // Reset to first page on refresh
                currentPage
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                if (lastItem == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                currentPage + 1 // Increment page for append
            }
        }

        Log.d(TAG, "Fetching movies from API for page: $page")

        return try {
            val response = apiService.getMovies(query = query, page = page)
            Log.d(TAG, "Response: ${response.search.size} movies fetched")

            val endOfPaginationReached = response.search.isEmpty()

            if (loadType == LoadType.REFRESH) {
                omdbDao.clearAllMovies()
                Log.d(TAG, "Cleared all movies from DB")
            }

            omdbDao.insertMovies(MovieDataMapper.mapMovieSearchResponseToMovieEntity(response))
            Log.d(TAG, "Inserted ${response.search.size} movies into DB")

            if (!endOfPaginationReached) {
                currentPage = page // Update current page if not end
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            Log.e(TAG, "IOException: ${e.message}", e)
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException: ${e.message}", e)
            MediatorResult.Error(e)
        }
    }
}