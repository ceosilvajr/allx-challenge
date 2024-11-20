package com.galton.movies.ui.pages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.LazyPagingItems
import com.galton.database.movie.MovieTable
import com.galton.movies.NavigationItem
import com.galton.movies.viewmodel.MovieViewModel

@Composable
fun ContentMain(
    viewModel: MovieViewModel,
    moviesPagingItems: LazyPagingItems<MovieTable>,
    favoritesPagingItems: LazyPagingItems<MovieTable>,
    navController: NavHostController,
    paddingValues: PaddingValues,
    onShowAppBar: (Boolean) -> Unit
) {
    NavHost(navController = navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            onShowAppBar.invoke(false)
            MoviesPage(
                modifier = Modifier.padding(paddingValues),
                pagingItems = moviesPagingItems,
                onMovieItemClicked = {
                    navController.navigate("${NavigationItem.MovieDetails.route}/${it.id}")
                },
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
        composable(NavigationItem.Favorite.route) {
            onShowAppBar.invoke(false)
            MoviesPage(
                modifier = Modifier.padding(paddingValues),
                pagingItems = favoritesPagingItems,
                onMovieItemClicked = {
                    navController.navigate("${NavigationItem.MovieDetails.route}/${it.id}")
                },
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
        composable(
            "${NavigationItem.MovieDetails.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            onShowAppBar.invoke(true)
            MovieDetailsPage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                backStackEntry = backStackEntry,
                viewModel = viewModel
            )
        }
    }
}
