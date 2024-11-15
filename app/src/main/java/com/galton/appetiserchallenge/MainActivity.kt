package com.galton.appetiserchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.galton.appetiserchallenge.pages.Greeting
import com.galton.network.NetworkManager
import com.galton.utils.MyAppTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val networkManager: NetworkManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        startNetworkObserver()
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
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
                Timber.d(getString(R.string.network_connected))
            } else {
                Timber.d(getString(R.string.network_disconnected))
            }
        }.launchIn(lifecycleScope)
        networkManager.startListenNetworkState()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkManager.stopListenNetworkState()
    }
}