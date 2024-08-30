package com.apps.omdbmovie.ui.component

import android.graphics.Movie
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.apps.omdbmovie.data.local.model.MovieEntity

@Composable
fun MovieListItem(movie: MovieEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            // Image
            Image(
                painter = rememberAsyncImagePainter(model = movie.poster),
                contentDescription = movie.title,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Text Information
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Year: ${movie.year}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "Type: ${movie.type}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieListItem() {
    Column {
        MovieListItem(
            movie = MovieEntity(
                imdbID = "tt0000001",
                type = "movie",
                poster = "https://m.media-amazon.com/images/M/MV5BMjMyNjgxNDc3MV5BMl5BanBnXkFtZTgwNDUyMzQ2NjM@._V1_SX300.jpg",
                title = "Carmencita",
                year = "1894"
            )
        )
        MovieListItem(
            movie = MovieEntity(
                imdbID = "tt0000001",
                type = "movie",
                poster = "https://m.media-amazon.com/images/I/71u6xuGqMRL._AC_SY679_.jpg",
                title = "Carmencita",
                year = "1894"
            )
        )
        MovieListItem(
            movie = MovieEntity(
                imdbID = "tt0000001",
                type = "movie",
                poster = "https://m.media-amazon.com/images/I/71u6xuGqMRL._AC_SY679_.jpg",
                title = "Carmencita",
                year = "1894"
            )
        )
    }

}