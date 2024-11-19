package com.galton.movies.ui.pages

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
import com.galton.movies.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun FavoritesPage(
    modifier: Modifier,
    viewModel: MovieViewModel,
    onMovieItemClicked: (Movie) -> Unit
) {
    val pagingItems = viewModel.favoriteMoviesPager().collectAsLazyPagingItems()

    FavoritesPage(
        modifier = modifier,
        pagingItems = pagingItems,
        onFavoriteItemClicked = { favorite, movie ->
            movie.id?.let {
                if (favorite) {
                    viewModel.addFavorite(it)
                } else {
                    viewModel.deleteFavorite(it)
                }
            }
        }, onMovieItemClicked
    )
}

@Composable
private fun FavoritesPage(
    modifier: Modifier,
    pagingItems: LazyPagingItems<MovieTable>,
    onFavoriteItemClicked: (Boolean, Movie) -> Unit,
    onMovieItemClicked: (Movie) -> Unit
) {
    val listState = rememberLazyListState()
    Box(modifier = modifier) {
        MovieListView(
            Modifier.padding(top = 16.dp),
            listState = listState,
            pagingItems = pagingItems,
            onFavoriteItemClicked = onFavoriteItemClicked,
            onMovieItemClicked
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
                    "Bradley Cooper"
                ),
                MovieTable(
                    0,
                    "A Star Is Born (2018)",
                    null,
                    "14.99",
                    "Romance",
                    "-",
                    false,
                    "Bradley Cooper"
                )
            )
        )
    )
    FavoritesPage(
        modifier = Modifier,
        pagingItems = list.collectAsLazyPagingItems(),
        onFavoriteItemClicked = { _, _ -> },
        onMovieItemClicked = {}
    )
}