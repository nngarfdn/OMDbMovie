package com.apps.omdbmovie.data.paging

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
    private val query: String,
    private val omdbDao: OmdbDao
) : PagingSource<Int, MovieEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
        val page = params.key ?: 1 // Start at the first page if no key is specified

        return try {
            // Fetch movies from the API
            val response = apiService.getMovies(query = query, page = page)

            // Map the response to a list of MovieEntity objects
            val movies = MovieDataMapper.mapMovieSearchResponseToMovieEntity(response)

            // Save the first page of data to Room
            if (page == 1) {
                omdbDao.insertMovies(movies)
            }

            // Return the loaded data and the next/previous keys
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            // Handle IO exceptions (e.g., network failures)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            // Handle HTTP exceptions (e.g., API errors)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? {
        // Return the key for the closest page to the current position
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
