package com.galton.movies.ui.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.galton.utils.MyAppTheme

@Composable
fun MoviesPage(name: String, modifier: Modifier = Modifier) {
    Text(name, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun MoviesPagePreview() {
    MyAppTheme {
        MoviesPage("Android!")
    }
}