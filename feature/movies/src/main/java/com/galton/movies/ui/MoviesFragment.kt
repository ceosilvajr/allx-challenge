package com.galton.movies.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.galton.movies.NavigationItem
import com.galton.movies.R
import com.galton.movies.movieInitialState
import com.galton.movies.ui.components.TabView
import com.galton.movies.ui.pages.FavoritesPage
import com.galton.movies.ui.pages.MovieDetailsPage
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
                        val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                        val allMoviesPagingItemsState = viewModel.moviesPager().collectAsLazyPagingItems()
                        val favoriteMoviesPagingItemsState = viewModel.favoriteMoviesPager().collectAsLazyPagingItems()

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
                                val density = LocalDensity.current
                                AnimatedVisibility(
                                    bottomBarState.value,
                                    enter = slideInVertically {
                                        with(density) { -30.dp.roundToPx() }
                                    } + expandVertically(
                                        expandFrom = Alignment.Top
                                    ) + fadeIn(
                                        initialAlpha = 0.3f
                                    ),
                                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                                ) {
                                    TabView(navController)
                                }
                            }
                        ) { paddingValues ->
                            NavHost(navController = navController, startDestination = NavigationItem.Home.route) {
                                composable(NavigationItem.Home.route) {
                                    bottomBarState.value = true
                                    var query: String? by rememberSaveable { mutableStateOf(null) }
                                    val searchedMoviesPagingItems =
                                        viewModel.moviesPager(query).collectAsLazyPagingItems()
                                    MoviesPage(
                                        modifier = Modifier.padding(paddingValues),
                                        query = query,
                                        allMoviesPagingItems = allMoviesPagingItemsState,
                                        searchedMoviesPagingItems = searchedMoviesPagingItems,
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
                                        },
                                        onMovieItemClicked = {
                                            navController.navigate("${NavigationItem.MovieDetails.route}/${it.id}")
                                        }
                                    )
                                }
                                composable(NavigationItem.Favorite.route) {
                                    bottomBarState.value = true
                                    FavoritesPage(
                                        modifier = Modifier.padding(paddingValues),
                                        allMoviesPagingItems = favoriteMoviesPagingItemsState,
                                        onFavoriteItemClicked = { favorite, movie ->
                                            movie.id?.let {
                                                if (favorite) {
                                                    viewModel.addFavorite(it)
                                                } else {
                                                    viewModel.deleteFavorite(it)
                                                }
                                            }
                                        },
                                        onMovieItemClicked = {
                                            navController.navigate("${NavigationItem.MovieDetails.route}/${it.id}")
                                        }
                                    )
                                }
                                composable(
                                    "${NavigationItem.MovieDetails.route}/{id}",
                                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                                ) { backStackEntry ->
                                    bottomBarState.value = false
                                    val cachedMovie =
                                        viewModel.getMovieById(backStackEntry.arguments?.getInt("id") ?: 0)
                                            ?.collectAsState(movieInitialState())
                                    if (cachedMovie != null) {
                                        MovieDetailsPage(
                                            modifier = Modifier.padding(paddingValues),
                                            movieState = cachedMovie,
                                            onFavoriteItemClicked = { favorite, movie ->
                                                movie.id?.let {
                                                    if (favorite) {
                                                        viewModel.addFavorite(it)
                                                    } else {
                                                        viewModel.deleteFavorite(it)
                                                    }
                                                }
                                            }
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
}