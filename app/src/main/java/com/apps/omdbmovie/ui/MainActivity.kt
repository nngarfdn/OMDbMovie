package com.apps.omdbmovie.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.apps.omdbmovie.ui.screen.movies.MovieViewModel
import com.apps.omdbmovie.ui.screen.movies.SearchScreen
import com.apps.omdbmovie.ui.theme.OMDbMovieTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OMDbMovieTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
//                        Greeting(
//                            modifier = Modifier.padding(innerPadding)
//                        )
                        SearchScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, movieViewModel: MovieViewModel = hiltViewModel()) {
    // Use LaunchedEffect to call getSearchMovies only once per query change
    LaunchedEffect("friends") {
        movieViewModel.getSearchMovies("friends")
    }

//    val movies = movieViewModel.searchMovies.collectAsLazyPagingItems()
//
//    Column(modifier = modifier.padding(16.dp)) {
//
//        LazyColumn {
//            items(movies.itemCount) { index ->
//                val movie = movies[index]
//                if (movie != null) {
//                    Text(text = movie.title)
//                }
//            }
//
//            // Handle loading more items when reaching the end
//            when (movies.loadState.append) {
//                is LoadState.Loading -> {
//                    item {
//                        CircularProgressIndicator(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                        )
//                    }
//                }
//                is LoadState.Error -> {
//                    item {
//                        Text("Error loading more movies", color = MaterialTheme.colorScheme.error)
//                    }
//                }
//                else -> {}
//            }
//        }
//    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OMDbMovieTheme {
//        Greeting()
    }
}