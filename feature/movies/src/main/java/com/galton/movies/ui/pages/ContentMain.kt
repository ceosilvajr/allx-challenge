package com.galton.movies.ui.pages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.galton.movies.NavigationItem
import com.galton.movies.viewmodel.MovieViewModel

@Composable
fun ContentMain(
    viewModel: MovieViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(navController = navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            MoviesPage(
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel,
                onMovieItemClicked = {
                    navController.navigate("${NavigationItem.MovieDetails.route}/${it.id}") {
                        popUpTo(NavigationItem.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(NavigationItem.Favorite.route) {
            FavoritesPage(
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel,
                onMovieItemClicked = {
                    navController.navigate("${NavigationItem.MovieDetails.route}/${it.id}") {
                        popUpTo(NavigationItem.Favorite.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(
            "${NavigationItem.MovieDetails.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            MovieDetailsPage(
                modifier = Modifier.padding(paddingValues),
                backStackEntry = backStackEntry,
                viewModel = viewModel
            )
        }
    }
}