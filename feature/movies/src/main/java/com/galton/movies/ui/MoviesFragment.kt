package com.galton.movies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.galton.movies.NavigationItem
import com.galton.movies.R
import com.galton.movies.ui.components.MovieSearchBar
import com.galton.movies.ui.components.TabView
import com.galton.movies.ui.components.TextView
import com.galton.movies.ui.pages.ContentMain
import com.galton.movies.viewmodel.MovieViewModel
import com.galton.utils.MyAppTheme

class MoviesFragment : Fragment() {

    private val viewModel: MovieViewModel by activityViewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel.getMovies()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyAppTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        var showAppBar by rememberSaveable { mutableStateOf(true) }
                        val moviesPagingItems = viewModel.moviesPager().collectAsLazyPagingItems()
                        val favoritesPagingItems = viewModel.favoriteMoviesPager().collectAsLazyPagingItems()

                        Scaffold(
                            topBar = {
                                if (showAppBar) {
                                    TopAppBar(
                                        colors = TopAppBarDefaults.topAppBarColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            titleContentColor = MaterialTheme.colorScheme.primary,
                                        ),
                                        title = {
                                            TextView(TextView.Model(stringId = R.string.title_top_bar))
                                        },
                                        navigationIcon = {
                                            IconButton(onClick = {
                                                navController.navigateUp()
                                            }) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    )

                                } else {
                                    MovieSearchBar(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp),
                                        viewModel = viewModel,
                                        onMovieItemClicked = {
                                            navController.navigate("${NavigationItem.MovieDetails.route}/${it}")
                                        }
                                    )
                                }
                            },
                            bottomBar = {
                                if (!showAppBar) {
                                    TabView(navController)
                                }
                            }
                        ) { paddingValues ->
                            ContentMain(
                                viewModel = viewModel,
                                moviesPagingItems = moviesPagingItems,
                                favoritesPagingItems = favoritesPagingItems,
                                navController = navController,
                                paddingValues = paddingValues,
                                onShowAppBar = {
                                    showAppBar = it
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}