package com.galton.movies.ui.pages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
    showTopNavIcon: MutableState<Boolean>,
    showBottomBar: MutableState<Boolean>,
    paddingValues: PaddingValues
) {
    NavHost(navController = navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            showBottomBar.value = true
            showTopNavIcon.value = false
            MoviesPage(
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel,
                onMovieItemClicked = {
                    navController.navigate("${NavigationItem.MovieDetails.route}/${it.id}")
                }
            )
        }
        composable(NavigationItem.Favorite.route) {
            showBottomBar.value = true
            showTopNavIcon.value = false
            FavoritesPage(
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel,
                onMovieItemClicked = {
                    navController.navigate("${NavigationItem.MovieDetails.route}/${it.id}")
                }
            )
        }
        composable(
            "${NavigationItem.MovieDetails.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            showBottomBar.value = false
            showTopNavIcon.value = true
            MovieDetailsPage(
                modifier = Modifier.padding(paddingValues),
                backStackEntry = backStackEntry,
                viewModel = viewModel
            )
        }
    }
}