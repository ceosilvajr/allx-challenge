package com.galton.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object DispatchersProvider {

    var unitTestDispatcher: CoroutineDispatcher? = null

    val IO
        get() = unitTestDispatcher ?: Dispatchers.IO

    val Default
        get() = unitTestDispatcher ?: Dispatchers.Default

    val Main
        get() = unitTestDispatcher ?: Dispatchers.Main

    val Unconfined
        get() = unitTestDispatcher ?: Dispatchers.Unconfined
}
