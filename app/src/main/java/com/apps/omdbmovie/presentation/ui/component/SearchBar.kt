package com.apps.omdbmovie.presentation.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search Movies") },
        placeholder = { Text("ex: friend") },
        textStyle = LocalTextStyle.current,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true,
    )
}


