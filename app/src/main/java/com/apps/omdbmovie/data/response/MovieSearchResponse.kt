package com.apps.omdbmovie.data.response


import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(
    @SerializedName("Response")
    val response: String = "",
    @SerializedName("Search")
    val search: List<Search> = listOf(),
    @SerializedName("totalResults")
    val totalResults: String = ""
)