package com.galton.movies.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.galton.database.movie.MovieTable
import com.galton.models.Movie
import com.galton.movies.R
import com.galton.movies.ui.components.MovieListView
import kotlinx.coroutines.flow.flowOf

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesPage(
    modifier: Modifier,
    query: String?,
    allMoviesPagingItems: LazyPagingItems<MovieTable>,
    searchedMoviesPagingItems: LazyPagingItems<MovieTable>,
    onFavoriteItemClicked: (Boolean, Movie) -> Unit,
    onSearchQueryChange: (String) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val moviesLazyListState = rememberLazyListState()

    Column(
        modifier = modifier,
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
            MovieListView(Modifier, rememberLazyListState(), searchedMoviesPagingItems, onFavoriteItemClicked)
        }
        if (!expanded) {
            MovieListView(
                Modifier.padding(top = 8.dp), moviesLazyListState, allMoviesPagingItems, onFavoriteItemClicked
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
        query = "",
        allMoviesPagingItems = list.collectAsLazyPagingItems(),
        searchedMoviesPagingItems = list.collectAsLazyPagingItems(),
        onFavoriteItemClicked = { _, _ -> },
        onSearchQueryChange = {}
    )
}