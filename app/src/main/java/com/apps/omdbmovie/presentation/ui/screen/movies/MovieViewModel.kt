package com.apps.omdbmovie.presentation.ui.screen.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.apps.omdbmovie.data.local.model.MovieEntity
import com.apps.omdbmovie.domain.usecase.GetMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieUseCase: GetMovieUseCase
) : ViewModel() {
    private val _searchMovies = MutableStateFlow<PagingData<MovieEntity>>(PagingData.empty())
    val searchMovies = _searchMovies.asStateFlow()
    fun getSearchMovies(query: String) {
        viewModelScope.launch {
            val result: Flow<PagingData<MovieEntity>> =
                movieUseCase.getMovies(query).cachedIn(viewModelScope)
            result.collectLatest { pagingData ->
                _searchMovies.value = pagingData
            }
        }
    }
}
