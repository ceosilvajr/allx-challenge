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
import com.galton.appetiserchallenge.ui.theme.AppetiserChallengeTheme
import com.galton.movies.MovieViewModel
import com.galton.utils.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getMovies()
        viewModel.moviesLiveData.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Log.d("MainActivity: SUCCESS", it.data.toString())
                }

                Resource.Status.ERROR -> {
                    Log.d("MainActivity: ERROR", it.message)
                }
                Resource.Status.NETWORK_DISCONNECTED -> {
                    Log.d("MainActivity: NETWORK_DISCONNECTED", it.message)
                }
                Resource.Status.LOADING -> {
                    Log.d("MainActivity: LOADING","....")
                }
            }
        }


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