package com.galton.appetiserchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.galton.appetiserchallenge.pages.Greeting
import com.galton.appetiserchallenge.ui.theme.AppetiserChallengeTheme
import com.galton.movies.viewmodel.MovieViewModel
import com.galton.network.NetworkManager
import com.galton.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val networkManager: NetworkManager by inject()
    private val viewModel: MovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startNetworkObserver()
        viewModel.moviesLiveData.observe(this) {
            Timber.d("DATA CACHED $it")
        }
        viewModel.moviesNetworkCall.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    // show hide loading
                    Timber.d("SUCCESS: ${it.data.toString()}")
                }

                Resource.Status.ERROR -> {
                    // show hide loading
                    Timber.d("ERROR ${it.message}")
                }

                Resource.Status.NETWORK_DISCONNECTED -> {
                    // show hide loading
                    Timber.d("NETWORK_DISCONNECTED ${it.message}")
                }

                Resource.Status.LOADING -> {
                    if (it.handled == false) {
                        Timber.d("MainActivity moviesLiveData LOADING ...")
                        // show loading
                        it.handled = true
                    }
                }
            }
        }
        viewModel.getMovies()
        viewModel.addFavorite("1727602354")
        enableEdgeToEdge()
        setContent {
            AppetiserChallengeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun startNetworkObserver() {
        networkManager.isNetworkConnectedFlow.onEach {
            if (it) {
                Timber.d("NETWORK CONNECTED")
            } else {
                Timber.d("NETWORK DISCONNECTED")
            }
        }.launchIn(lifecycleScope)
        networkManager.startListenNetworkState()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkManager.stopListenNetworkState()
    }
}