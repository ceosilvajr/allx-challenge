package com.galton.movies.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.galton.movies.R
import com.galton.movies.ui.components.FAVORITE_TAB_ID
import com.galton.movies.ui.components.HOME_TAB_ID
import com.galton.movies.ui.components.TabView
import com.galton.movies.ui.pages.FavoritesPage
import com.galton.movies.ui.pages.MoviesPage
import com.galton.movies.viewmodel.MovieViewModel
import com.galton.utils.MyAppTheme

class MoviesFragment : Fragment() {

    private val viewModel: MovieViewModel by activityViewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                        Scaffold(
                            topBar = {
                                MediumTopAppBar(
                                    colors = topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        titleContentColor = MaterialTheme.colorScheme.primary,
                                    ),
                                    title = {
                                        Text(stringResource(R.string.title_top_bar))
                                    }
                                )
                            },
                            bottomBar = {
                                TabView(navController)
                            }
                        ) { paddingValues ->
                            NavHost(navController = navController, startDestination = HOME_TAB_ID) {
                                composable(HOME_TAB_ID) {
                                    var query: String? by rememberSaveable { mutableStateOf(null) }
                                    MoviesPage(
                                        modifier = Modifier.padding(paddingValues),
                                        query = query,
                                        allMoviesPagingItems = viewModel.moviesPager().collectAsLazyPagingItems(),
                                        searchedMoviesPagingItems = viewModel.moviesPager(query)
                                            .collectAsLazyPagingItems(),
                                        onFavoriteItemClicked = { favorite, movie ->
                                            movie.id?.let {
                                                if (favorite) {
                                                    viewModel.addFavorite(it)
                                                } else {
                                                    viewModel.deleteFavorite(it)
                                                }
                                            }
                                        },
                                        onSearchQueryChange = {
                                            query = it.ifEmpty { null }
                                        }
                                    )
                                }
                                composable(FAVORITE_TAB_ID) {
                                    FavoritesPage(
                                        modifier = Modifier.padding(paddingValues),
                                        viewModel.favoriteMoviesPager().collectAsLazyPagingItems(),
                                        onFavoriteItemClicked = { favorite, movie ->
                                            movie.id?.let {
                                                if (favorite) {
                                                    viewModel.addFavorite(it)
                                                } else {
                                                    viewModel.deleteFavorite(it)
                                                }
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}