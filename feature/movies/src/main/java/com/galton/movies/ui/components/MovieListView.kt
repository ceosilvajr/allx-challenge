package com.galton.movies.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.galton.database.movie.MovieTable
import com.galton.models.Movie
import com.galton.movies.R
import com.galton.movies.toMovie
import kotlinx.coroutines.flow.flowOf

@Composable
fun MovieListView(
    modifier: Modifier,
    pagingItems: LazyPagingItems<MovieTable>,
    onFavoriteItemClicked: (Boolean, Movie) -> Unit,
    onMovieItemClicked: (Movie) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = listState,
        content = {
            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey {
                    "${it.id}${it.updatedAt}"
                }
            ) { index ->
                val movie = pagingItems[index]?.toMovie()
                if (movie != null) {
                    MovieItemView(
                        modifier = Modifier.animateItem(),
                        movie = movie,
                        onFavoriteItemClicked = { favorite, m ->
                            onFavoriteItemClicked.invoke(favorite, m)
                        },
                        onMovieItemClicked = onMovieItemClicked
                    )
                }
            }
            pagingItems.apply {
                when {
                    loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                        item {
                            // Loading component can be shown here.
                        }
                    }

                    loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached && itemCount > 0 -> {
                        item {
                            EmptyView(R.string.message_end_list)
                        }
                    }

                    loadState.append is LoadState.NotLoading && itemCount == 0 -> {
                        item {
                            EmptyView(R.string.message_empty_list)
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        item {
                            EmptyView(R.string.message_something_wrong)
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun MovieListViewPreview() {
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
                    updatedAt = 0L
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
                    updatedAt = 0L
                )
            )
        )
    )
    MovieListView(
        modifier = Modifier,
        pagingItems = list.collectAsLazyPagingItems(),
        onFavoriteItemClicked = { _, _ -> },
        onMovieItemClicked = {}
    )
}
