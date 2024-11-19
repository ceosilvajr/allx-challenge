package com.galton.movies.ui.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.galton.movies.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchBar(
    modifier: Modifier = Modifier,
    queryState: MutableState<String?>,
    content: @Composable (expanded: Boolean) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = modifier,
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
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        content(expanded)
    }
}

@Preview
@Composable
fun MovieSearchBarPreview() {
    MovieSearchBar(
        queryState = rememberSaveable { mutableStateOf<String?>(null) }
    ) {

    }
}