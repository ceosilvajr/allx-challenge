package com.galton.movies

enum class Page {
    HOME,
    FAVORITE,
    MOVIE_DETAILS,
}

sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Page.HOME.name)
    object Favorite : NavigationItem(Page.FAVORITE.name)
    object MovieDetails : NavigationItem(Page.MOVIE_DETAILS.name)
}