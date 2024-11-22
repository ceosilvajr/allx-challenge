package com.galton.movies.ui.components

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.galton.movies.ui.NavigationItem
import com.galton.movies.R

data class TabBarItem(
    val id: String,
    val title: String,
    val icon: ImageVector
)

@SuppressLint("RestrictedApi")
@Composable
fun TabView(navController: NavController) {
    val home = TabBarItem(
        id = NavigationItem.Home.route,
        title = stringResource(R.string.tab_title_home),
        icon = Icons.Outlined.Home
    )
    val favorites = TabBarItem(
        id = NavigationItem.Favorite.route,
        title = stringResource(R.string.tab_title_favorites),
        icon = Icons.Outlined.FavoriteBorder
    )
    val tabBarItems = listOf(home, favorites)

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.hasRoute(tabBarItem.id, null) } == true,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(tabBarItem.id) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = tabBarItem.icon,
                        contentDescription = tabBarItem.title
                    )
                },
                label = {
                    TextView(TextView.Model(string = tabBarItem.title, textSizes = TextSizes.DESCRIPTION))
                }
            )
        }
    }
}
