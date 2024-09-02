package com.apps.omdbmovie.data.paging

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
    private var currentPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {

        // Determine the page number
        val page = when (loadType) {
            LoadType.REFRESH -> {
                currentPage = 1 // Reset to first page on refresh
                currentPage
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                currentPage + 1 // Increment page for append
            }
        }

        return try {
            val response = apiService.getMovies(query = query, page = page)

            val endOfPaginationReached = response.search.isEmpty()

            if (loadType == LoadType.REFRESH) {
                omdbDao.clearAllMovies()
            }

            omdbDao.insertMovies(MovieDataMapper.mapMovieSearchResponseToMovieEntity(response))

            if (!endOfPaginationReached) {
                currentPage = page // Update current page if not end
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}