package com.galton.movies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.compose.collectAsLazyPagingItems
import com.galton.movies.ui.pages.MoviesPage
import com.galton.movies.viewmodel.MovieViewModel
import com.galton.utils.MyAppTheme

class MoviesFragment : Fragment() {

    private val viewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel.getMovies()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyAppTheme {
                    var query: String? by rememberSaveable { mutableStateOf(null) }
                    MoviesPage(
                        query = query,
                        allMoviesPagingItems = viewModel.moviesPager().collectAsLazyPagingItems(),
                        searchedMoviesPagingItems = viewModel.moviesPager(query).collectAsLazyPagingItems(),
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
            }
        }
    }
}