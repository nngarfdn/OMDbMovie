package com.apps.omdbmovie.ui.screen.movies

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.apps.omdbmovie.ui.component.MovieListItem
import com.apps.omdbmovie.ui.component.ShimmerMovieListItem
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier, viewModel: MovieViewModel = hiltViewModel()) {
    var query by remember { mutableStateOf("") }
    val debounceQuery = remember { MutableStateFlow("") }

    LaunchedEffect(query) {
        debounceQuery.value = query
    }

    LaunchedEffect(debounceQuery) {
        debounceQuery
            .debounce(500)
            .distinctUntilChanged()
            .collect { searchQuery ->
                if (searchQuery.isNotEmpty()) {
                    viewModel.getSearchMovies(searchQuery)
                }
            }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Search Input
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search Movies") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (query.isEmpty()) {
            // Show initial message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Please input search value", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            // Observe the movies list from the ViewModel
            val moviesPagingData = viewModel.searchMovies.collectAsLazyPagingItems()

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // Show shimmer items while loading initially
                if (moviesPagingData.loadState.refresh is LoadState.Loading) {
                    items(10) {
                        ShimmerMovieListItem()
                    }
                }

                // Display loaded movies
                items(moviesPagingData.itemCount) { position ->
                    val movie = moviesPagingData[position]
                    if (movie != null) {
                        MovieListItem(movie = movie)
                    }
                }

                // Show shimmer item when loading the next page
                if (moviesPagingData.loadState.append is LoadState.Loading) {
                    item {
                        ShimmerMovieListItem()
                    }
                }

                // Handle error state
                if (moviesPagingData.loadState.refresh is LoadState.Error) {
                    val e = moviesPagingData.loadState.refresh as LoadState.Error
                    item {
                        Text(
                            text = "Error: ${e.error.localizedMessage}",
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}