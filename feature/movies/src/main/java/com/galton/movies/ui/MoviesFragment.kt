package com.galton.movies.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.compose.rememberNavController
import com.galton.movies.R
import com.galton.movies.ui.components.TabView
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
                        val showBottomBar = rememberSaveable { (mutableStateOf(true)) }
                        val showTopNavIcon = rememberSaveable { (mutableStateOf(true)) }

                        Scaffold(
                            topBar = {
                                MediumTopAppBar(
                                    colors = topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        titleContentColor = MaterialTheme.colorScheme.primary,
                                    ),
                                    title = {
                                        Text(stringResource(R.string.title_top_bar))
                                    },
                                    navigationIcon = {
                                        if (showTopNavIcon.value) {
                                            IconButton(onClick = { navController.navigateUp() }) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                )
                            },
                            bottomBar = {
                                val density = LocalDensity.current
                                AnimatedVisibility(
                                    showBottomBar.value,
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
                            ContentMain(
                                viewModel = viewModel,
                                navController = navController,
                                showBottomBar = showBottomBar,
                                showTopNavIcon = showTopNavIcon,
                                paddingValues = paddingValues
                            )
                        }
                    }
                }
            }
        }
    }
}