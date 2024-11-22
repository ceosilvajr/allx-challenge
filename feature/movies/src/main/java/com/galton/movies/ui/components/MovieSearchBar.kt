package com.galton.movies.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.galton.database.movie.MovieTable
import com.galton.movies.R
import com.galton.movies.data.toMovie
import com.galton.movies.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun MovieSearchBar(
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel,
    onMovieItemClicked: (String) -> Unit
) {
    val expanded = rememberSaveable { mutableStateOf(false) }
    val queryState = rememberSaveable { mutableStateOf<String?>(null) }
    val pagingItems = viewModel.moviesPager(queryState.value).collectAsLazyPagingItems()

    MovieSearchBar(modifier, expanded, queryState, pagingItems, onMovieItemClicked)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieSearchBar(
    modifier: Modifier,
    expanded: MutableState<Boolean>,
    queryState: MutableState<String?>,
    pagingItems: LazyPagingItems<MovieTable>,
    onMovieItemClicked: (String) -> Unit
) {
    SearchBar(
        modifier = modifier,
        inputField = {
            SearchBarDefaults.InputField(
                onSearch = { expanded.value = false },
                expanded = expanded.value,
                onExpandedChange = { expanded.value = it },
                placeholder = { Text(stringResource(R.string.search)) },
                leadingIcon = {
                    if (expanded.value) {
                        IconButton(
                            onClick = { expanded.value = false },
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
                    if (queryState.value.isNullOrEmpty().not()) {
                        IconButton(
                            onClick = { queryState.value = null },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                            )
                        }
                    }
                },
                onQueryChange = { queryState.value = it.ifEmpty { null } },
                query = queryState.value ?: ""
            )
        },
        expanded = expanded.value,
        onExpandedChange = { expanded.value = it },
    ) {
        LazyColumn {
            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.id }
            ) { index ->
                val movie = pagingItems[index]?.toMovie()
                if (movie != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                expanded.value = false
                                queryState.value = null
                                onMovieItemClicked.invoke(movie.id.toString())
                            }
                    ) {
                        TextView(
                            TextView.Model(
                                modifier = Modifier,
                                string = movie.trackName,
                                textSizes = TextSizes.DETAILS
                            )
                        )
                    }
                }
            }
            pagingItems.apply {
                when {
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
    }
}

@Preview
@Composable
fun MovieSearchBarPreview() {
    MovieSearchBar(
        modifier = Modifier,
        expanded = rememberSaveable { mutableStateOf(false) },
        queryState = rememberSaveable { mutableStateOf<String?>(null) },
        pagingItems = flowOf(PagingData.from(listOf<MovieTable>())).collectAsLazyPagingItems(),
        onMovieItemClicked = {}
    )
}
