package com.apps.omdbmovie.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apps.omdbmovie.data.local.db.OmdbDao
import com.apps.omdbmovie.data.local.model.MovieEntity
import com.apps.omdbmovie.data.mapper.MovieDataMapper
import com.apps.omdbmovie.data.network.ApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MoviePagingSource @Inject constructor(
    private val apiService: ApiService,
    private val query: String
) : PagingSource<Int, MovieEntity>() {
    private val TAG = "MoviePagingSource"

    init {
        Log.d(TAG, "MoviePagingSource initialized with query: $query")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
        val page = params.key ?: 1 // Start from the first page if no key is specified
        Log.d(TAG, "load method called with page: $page")

        return try {
            // Fetch movies from the API
            val response = apiService.getMovies(query = query, page = page)
            Log.d(TAG, "Response: ${response.search.size} movies fetched")

            // Map response to a list of MovieEntity objects
            val movies = MovieDataMapper.mapMovieSearchResponseToMovieEntity(response)

            // Return the loaded data and next/previous keys
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            Log.e(TAG, "IOException: ${e.message}", e)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException: ${e.message}", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? {
        Log.d(TAG, "getRefreshKey called")
        // Return the key for the page closest to the current position
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
