package com.apps.omdbmovie.ui.utils

import androidx.paging.PagingData
import com.apps.omdbmovie.data.local.model.MovieEntity

sealed class MovieUiState {
    object Loading : MovieUiState()
    object Empty : MovieUiState()
    data class Success(val data: PagingData<MovieEntity>) : MovieUiState()
    data class Error(val message: String) : MovieUiState()
}
