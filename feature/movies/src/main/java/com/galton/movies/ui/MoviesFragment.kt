package com.galton.movies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.galton.movies.ui.pages.MoviesPage
import com.galton.movies.viewmodel.MovieViewModel
import com.galton.utils.MyAppTheme
import com.galton.utils.Resource
import timber.log.Timber

class MoviesFragment : Fragment() {

    private val viewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        viewModel.moviesLiveData.observe(viewLifecycleOwner) {
            Timber.d("DATA CACHED $it")
        }
        viewModel.moviesNetworkCall.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Timber.d("SUCCESS: ${it.data.toString()}")
                }

                Resource.Status.ERROR -> {
                    Timber.d("ERROR ${it.message}")
                }

                Resource.Status.NETWORK_DISCONNECTED -> {
                    Timber.d("NETWORK_DISCONNECTED ${it.message}")
                }

                Resource.Status.LOADING -> {
                    if (it.handled == false) {
                        Timber.d("MainActivity moviesLiveData LOADING ...")
                        it.handled = true
                    }
                }
            }
        }
        viewModel.getMovies()
        viewModel.addFavorite("1727602354")
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyAppTheme {
                    MoviesPage("Success Navigation to MoviesFragment!")
                }
            }
        }
    }
}