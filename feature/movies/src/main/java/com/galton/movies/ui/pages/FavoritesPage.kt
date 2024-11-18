package com.galton.movies.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.galton.database.movie.MovieTable
import com.galton.models.Movie
import com.galton.movies.ui.components.MovieListView
import kotlinx.coroutines.flow.flowOf

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoritesPage(
    modifier: Modifier,
    allMoviesPagingItems: LazyPagingItems<MovieTable>,
    onFavoriteItemClicked: (Boolean, Movie) -> Unit
) {
    Box(modifier = modifier) {
        val moviesLazyListState = rememberLazyListState()
        MovieListView(
            Modifier.padding(top = 8.dp), moviesLazyListState, allMoviesPagingItems, onFavoriteItemClicked
        )
    }
}

@Preview
@Composable
fun FavoritesPagePreview() {
    val list = flowOf(
        PagingData.from(
            listOf(
                MovieTable(
                    0,
                    "A Star Is Born (2018)",
                    null,
                    "14.99",
                    "Romance",
                    "-",
                    true,
                    "Bradley Cooper",
                ),
                MovieTable(
                    0,
                    "A Star Is Born (2018)",
                    null,
                    "14.99",
                    "Romance",
                    "-",
                    false,
                    "Bradley Cooper",
                )
            )
        )
    )
    FavoritesPage(
        modifier = Modifier,
        allMoviesPagingItems = list.collectAsLazyPagingItems(),
        onFavoriteItemClicked = { _, _ -> }
    )
}