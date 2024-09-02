package com.apps.omdbmovie.ui.screen.movies

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apps.omdbmovie.presentation.ui.MainActivity
import com.apps.omdbmovie.presentation.ui.component.ShimmerMovieListItem
import com.apps.omdbmovie.presentation.ui.screen.movies.MovieViewModel
import com.apps.omdbmovie.presentation.ui.screen.movies.SearchScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testSearchBarInput() {
        composeTestRule.setContent {
            SearchScreen(viewModel = hiltViewModel<MovieViewModel>())
        }
    }
}