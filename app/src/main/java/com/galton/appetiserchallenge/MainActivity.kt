package com.galton.appetiserchallenge

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.galton.appetiserchallenge.ui.theme.AppetiserChallengeTheme
import com.galton.movies.MovieViewModel
import com.galton.network.NetworkManager
import com.galton.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val networkManager: NetworkManager by inject()
    private val viewModel: MovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkManager.startListenNetworkState()
        networkManager.isNetworkConnectedFlow.onEach {
            if (it) {
                Log.d("MainActivity isNetworkConnectedFlow", "NETWORK CONNECTED")
            } else {
                Log.d("MainActivity isNetworkConnectedFlow", "NETWORK DISCONNECTED")
            }
        }.launchIn(lifecycleScope)
        viewModel.moviesLiveData.observe(this) {
            Log.d("MainActivity moviesLiveData: SUCCESS", it.toString())
        }
        viewModel.moviesNetworkCall.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    // show hide loading
                    Log.d("MainActivity moviesLiveData: SUCCESS", it.data.toString())
                }

                Resource.Status.ERROR -> {
                    // show hide loading
                    Log.d("MainActivity moviesLiveData:", "ERROR ${it.message}")
                }

                Resource.Status.NETWORK_DISCONNECTED -> {
                    // show hide loading
                    Log.d("MainActivity moviesLiveData:", "NETWORK_DISCONNECTED ${it.message}")
                }

                Resource.Status.LOADING -> {
                    if (it.handled == false) {
                        Log.d("MainActivity moviesLiveData", "LOADING ...")
                        // show loading
                        it.handled = true
                    }
                }
            }
        }
        viewModel.addFavorite("1727602354") // favorites is not working as expected.
        viewModel.getMovies()
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

    override fun onDestroy() {
        super.onDestroy()
        networkManager.stopListenNetworkState()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppetiserChallengeTheme {
        Greeting("Android")
    }
}