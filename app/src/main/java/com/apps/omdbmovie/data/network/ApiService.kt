package com.apps.omdbmovie.data.network

import com.apps.omdbmovie.data.response.MovieSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/")
    suspend fun getMovies(
        @Query("s") query: String,
        @Query("apikey") apiKey: String = "761c695e",
        @Query("page") page: Int
    ): MovieSearchResponse
}