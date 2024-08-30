package com.apps.omdbmovie.ui.screen.movies

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.apps.omdbmovie.ui.component.MovieListItem
import com.apps.omdbmovie.ui.component.ShimmerMovieListItem
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier, viewModel: MovieViewModel = hiltViewModel()) {
    var query by remember { mutableStateOf("friend") }
    var retry by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Connectivity state
    val isConnected = remember { mutableStateOf(checkInternetConnection(context)) }

    // Observe connectivity changes
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
        // Search Input
        TextField(
            value = query,
            onValueChange = {
                query = it
                retry = false // Reset retry when the query changes
            },
            label = { Text("Search Movies") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Debounce logic using LaunchedEffect
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
                if (moviesPagingData.loadState.refresh is LoadState.Error || !isConnected.value) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (!isConnected.value) {
                                    "No internet connection. Please check your connection."
                                } else {
                                    "Error: Unable to load data."
                                },
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                if (isConnected.value) {
                                    retry = true // Retry fetching movies
                                    viewModel.getSearchMovies(query)
                                } else {
                                    // Notify user to check connectivity
                                    // Optional: Display a Snackbar or Toast message here
                                }
                            }) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper function to check internet connection
fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}

// Function to observe connectivity changes
fun observeConnectivityAsFlow(context: Context): StateFlow<Boolean> {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkRequest = NetworkRequest.Builder().build()
    val _connectivityStatus = MutableStateFlow(checkInternetConnection(context))

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _connectivityStatus.value = true
        }

        override fun onLost(network: Network) {
            _connectivityStatus.value = false
        }
    }

    connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

    return _connectivityStatus
}


