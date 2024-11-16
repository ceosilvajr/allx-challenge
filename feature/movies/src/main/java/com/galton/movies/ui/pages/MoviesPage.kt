package com.galton.movies.ui.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galton.database.movie.MovieTable
import com.galton.movies.toMovie
import com.galton.movies.ui.components.MovieItemView
import com.galton.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoviesPage(
    list: Flow<List<MovieTable>>,
    network: Flow<Resource<Boolean>>
) {
    val listState = rememberLazyListState()
    val state = list.collectAsState(initial = emptyList())
    val networkState = network.collectAsState(initial = Resource.loading())

    LazyColumn(state = listState) {
        stickyHeader {
            Box(modifier = Modifier.size(80.dp)) {
                // Do nothing
            }
        }
        itemsIndexed(state.value) { _, item ->
            MovieItemView(item.toMovie())
        }
        item {
            Box(modifier = Modifier.size(80.dp)) {
                // Footer
            }
        }
    }
}

@Preview
@Composable
fun MoviesPagePreview() {
    val list = flowOf(
        listOf(
            MovieTable(
                "-",
                "A Star Is Born (2018)",
                null,
                "AUD 14.99",
                "Romance",
                "-",
                true,
                "Bradley Cooper",
            ),
            MovieTable(
                "-",
                "A Star Is Born (2018)",
                null,
                "AUD 14.99",
                "Romance",
                "-",
                false,
                "Bradley Cooper",
            )
        )
    )
    MoviesPage(list, flowOf(Resource.loading()))
}