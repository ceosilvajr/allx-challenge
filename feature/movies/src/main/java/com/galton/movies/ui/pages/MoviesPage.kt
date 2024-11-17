package com.galton.movies.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
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
import com.galton.movies.ui.components.MovieItemView
import com.galton.movies.ui.components.TextView
import kotlinx.coroutines.flow.flowOf

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesPage(
    query: String?,
    allMoviesPagingItems: LazyPagingItems<MovieTable>,
    searchedMoviesPagingItems: LazyPagingItems<MovieTable>,
    onFavoriteItemClicked: (Boolean, Movie) -> Unit,
    onSearchQueryChange: (String) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .semantics { isTraversalGroup = true }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .semantics { traversalIndex = 0f },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            onSearch = { expanded = false },
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            placeholder = { Text(stringResource(R.string.search)) },
                            leadingIcon = {
                                if (expanded) {
                                    IconButton(
                                        onClick = { expanded = false },
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = null,
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                    )
                                }
                            },
                            trailingIcon = {
                                if (query.isNullOrEmpty().not()) {
                                    IconButton(
                                        onClick = { onSearchQueryChange.invoke("") },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = null,
                                        )
                                    }
                                }
                            },
                            onQueryChange = onSearchQueryChange,
                            query = query ?: ""
                        )
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {
                    MovieList(searchedMoviesPagingItems, onFavoriteItemClicked)
                }
                if (!expanded) {
                    MovieList(allMoviesPagingItems, onFavoriteItemClicked)
                }
            }
        }
    }
}

@Composable
fun MovieList(
    pagingItems: LazyPagingItems<MovieTable>,
    onFavoriteItemClicked: (Boolean, Movie) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = rememberLazyListState(),
        content = {
            items(
                pagingItems.itemCount,
                key = pagingItems.itemKey { it.id }
            ) { index ->
                val movie = pagingItems[index]?.toMovie()
                if (movie != null) {
                    MovieItemView(movie) { favorite, m ->
                        onFavoriteItemClicked.invoke(favorite, m)
                    }
                }
            }
            pagingItems.apply {
                when {
                    loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                        item {
                            // Display Loader here.
                        }
                    }

                    loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached -> {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp)
                            ) {
                                TextView(
                                    TextView.Model(
                                        string = "Nothing to show here"
                                    )
                                )
                            }
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp)
                            ) {
                                TextView(
                                    TextView.Model(
                                        string = "Something went wrong"
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    )
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
                    "AUD 14.99",
                    "Romance",
                    "-",
                    true,
                    "Bradley Cooper",
                ),
                MovieTable(
                    0,
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
    )
    MoviesPage(
        "",
        list.collectAsLazyPagingItems(),
        list.collectAsLazyPagingItems(),
        onFavoriteItemClicked = { _, _ -> },
        onSearchQueryChange = {}
    )
}