package com.galton.movies.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.galton.database.movie.MovieTable
import com.galton.models.Movie
import com.galton.movies.ui.components.MovieListView
import com.galton.movies.ui.components.MovieSearchBar
import com.galton.movies.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun MoviesPage(
    modifier: Modifier,
    onMovieItemClicked: (Movie) -> Unit,
    viewModel: MovieViewModel
) {
    val expandedState = rememberSaveable { mutableStateOf(false) }
    val queryState = rememberSaveable { mutableStateOf<String?>(null) }
    val allMoviesPagingItems = viewModel.moviesPager().collectAsLazyPagingItems()
    val searchMoviesPagingItems = viewModel.moviesPager(queryState.value).collectAsLazyPagingItems()

    MoviesPage(
        modifier = modifier,
        allMoviesPagingItems = allMoviesPagingItems,
        searchMoviesPagingItems = searchMoviesPagingItems,
        queryState = queryState,
        expandedState = expandedState,
        onFavoriteItemClicked = { favorite, movie ->
            movie.id?.let {
                if (favorite) {
                    viewModel.addFavorite(it)
                } else {
                    viewModel.deleteFavorite(it)
                }
            }
        },
        onMovieItemClicked = onMovieItemClicked
    )
}

@Composable
private fun MoviesPage(
    modifier: Modifier,
    allMoviesPagingItems: LazyPagingItems<MovieTable>,
    searchMoviesPagingItems: LazyPagingItems<MovieTable>,
    queryState: MutableState<String?>,
    expandedState: MutableState<Boolean>,
    onFavoriteItemClicked: (Boolean, Movie) -> Unit,
    onMovieItemClicked: (Movie) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MovieSearchBar(Modifier, queryState) { expanded ->
            expandedState.value = expanded

            MovieListView(
                Modifier,
                listState = rememberLazyListState(),
                pagingItems = searchMoviesPagingItems,
                onFavoriteItemClicked = onFavoriteItemClicked,
                onMovieItemClicked
            )
        }
        if (!expandedState.value) {
            MovieListView(
                Modifier.padding(top = 16.dp),
                listState = rememberLazyListState(),
                pagingItems = allMoviesPagingItems,
                onFavoriteItemClicked = onFavoriteItemClicked,
                onMovieItemClicked
            )
        }
    }
}

@Preview
@Composable
fun MoviesPagePreview() {
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
    MoviesPage(
        modifier = Modifier,
        allMoviesPagingItems = list.collectAsLazyPagingItems(),
        searchMoviesPagingItems = list.collectAsLazyPagingItems(),
        queryState = rememberSaveable { mutableStateOf<String?>(null) },
        expandedState = rememberSaveable { mutableStateOf(false) },
        onFavoriteItemClicked = { _, _ -> },
        onMovieItemClicked = {}
    )
}