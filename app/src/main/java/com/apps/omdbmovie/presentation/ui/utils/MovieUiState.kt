package com.apps.omdbmovie.presentation.ui.utils

import androidx.paging.PagingData
import com.apps.omdbmovie.data.local.model.MovieEntity

sealed class MovieUiState {
    data object Loading : MovieUiState()
    data object Empty : MovieUiState()
    data class Success(val data: PagingData<MovieEntity>) : MovieUiState()
    data class Error(val message: String) : MovieUiState()
}
