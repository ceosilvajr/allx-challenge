package com.galton.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.galton.utils.DispatchersProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.koin.test.KoinTest

@OptIn(ExperimentalCoroutinesApi::class)
open class UnitTest : KoinTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    protected val testDispatcher = UnconfinedTestDispatcher()

    init {
        DispatchersProvider.unitTestDispatcher = testDispatcher
        Dispatchers.setMain(testDispatcher)
    }
}
