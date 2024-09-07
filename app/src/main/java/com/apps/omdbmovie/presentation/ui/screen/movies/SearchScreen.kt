package com.apps.omdbmovie.presentation.ui.screen.movies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.apps.omdbmovie.presentation.ui.component.EmptySearchComponent
import com.apps.omdbmovie.presentation.ui.component.ErrorStateComponent
import com.apps.omdbmovie.presentation.ui.component.MovieListItem
import com.apps.omdbmovie.presentation.ui.component.SearchBar
import com.apps.omdbmovie.presentation.ui.component.ShimmerMovieListItem
import com.apps.omdbmovie.presentation.ui.utils.checkInternetConnection
import com.apps.omdbmovie.presentation.ui.utils.observeConnectivityAsFlow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier, viewModel: MovieViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    var retry by remember { mutableStateOf(false) }
    val isConnected = remember { mutableStateOf(checkInternetConnection(context)) }

    LaunchedEffect(Unit) {
        observeConnectivityAsFlow(context).collect { connected ->
            isConnected.value = connected
            if (connected && retry) {
                viewModel.getSearchMovies(query) // Refresh data when reconnected
                retry = false
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            query = query,
            onQueryChange = {
                query = it
                retry = false // Reset retry when the query changes
            }
        )

        LaunchedEffect(query) {
            snapshotFlow { query }
                .debounce(500) // 500ms debounce
                .distinctUntilChanged()
                .collect { debouncedQuery ->
                    viewModel.getSearchMovies(debouncedQuery)
                }
        }

        if (query.isEmpty()) {
            // Show initial message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Please input search value", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            val moviesPagingData = viewModel.searchMovies.collectAsLazyPagingItems()

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                when {
                    moviesPagingData.loadState.refresh is LoadState.Loading -> {
                        items(10) {
                            ShimmerMovieListItem()
                        }
                    }
                    moviesPagingData.itemCount == 0 && moviesPagingData.loadState.refresh !is LoadState.Loading -> {
                        item {
                            EmptySearchComponent(query = query)
                        }
                    }
                    else -> {
                        // Display loaded movies
                        items(moviesPagingData.itemCount) { position ->
                            val movie = moviesPagingData[position]
                            if (movie != null) {
                                MovieListItem(movie = movie)
                            }
                        }
                    }
                }

                // Show shimmer item when loading the next page
                if (moviesPagingData.loadState.append is LoadState.Loading) {
                    item {
                        ShimmerMovieListItem()
                    }
                }

                // Use the separated error state component
                if (moviesPagingData.loadState.refresh is LoadState.Error || !isConnected.value) {
                    item {
                        ErrorStateComponent(
                            isConnected = isConnected.value,
                            onRetry = {
                                if (isConnected.value) {
                                    retry = true // Retry fetching movies
                                    viewModel.getSearchMovies(query)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}