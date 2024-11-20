package com.galton.appetiserchallenge

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.galton.network.NetworkManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity(R.layout.layout_main_activity) {

    private val networkManager: NetworkManager by inject()

    /**
     * Provides navigation capabilities within the app.
     *
     * This `NavController` is used to navigate between different fragments or
     * activities within the application. Currently, it primarily supports
     * navigation to the `MoviesFragment`. However, it is designed to be
     * easily extensible for handling deep links and navigating to other
     * features in the future.
     */
    val navController by lazy {
        findNavController(R.id.app_nav)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        startNetworkObserver()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
