package com.apps.omdbmovie.ui.screen.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.paging.map
import com.apps.omdbmovie.data.local.model.MovieEntity
import com.apps.omdbmovie.domain.usecase.GetMovieUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class MovieViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var movieViewModel: MovieViewModel
    private val movieUseCase: GetMovieUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        movieViewModel = MovieViewModel(movieUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getSearchMovies should return empty data`() = runTest {
        // Given
        val query = "test query"
        val expectedMovies = mutableListOf<MovieEntity>()
        val pagingData = PagingData.from(expectedMovies)
        val searchMovies = MutableStateFlow(pagingData)

        coEvery { movieUseCase.getMovies(query) } returns searchMovies

        // When
        movieViewModel.getSearchMovies(query)

        // Then
        val actualMovies = mutableListOf<MovieEntity>()

        movieViewModel.searchMovies.value.map {
            actualMovies.add(it)
        }

        assertEquals(expectedMovies, actualMovies)
    }


}
