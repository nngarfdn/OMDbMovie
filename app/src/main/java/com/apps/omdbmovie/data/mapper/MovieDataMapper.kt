package com.apps.omdbmovie.data.mapper

import com.apps.omdbmovie.data.local.model.MovieEntity
import com.apps.omdbmovie.data.response.MovieSearchResponse

object MovieDataMapper {
    fun mapMovieSearchResponseToMovieEntity(movieSearchResponse: MovieSearchResponse):
            List<MovieEntity> {
        return movieSearchResponse.search.map {
            MovieEntity(
                title = it.title,
                year = it.year,
                imdbID = it.imdbID,
                type = it.type,
                poster = it.poster
            )
        }
    }
}