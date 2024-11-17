package com.galton.movies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asFlow
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
                    MoviesPage(
                        viewModel.moviesLiveData.asFlow(),
                        viewModel.moviesNetworkCall.asFlow()
                    )
                }
            }
        }
    }
}