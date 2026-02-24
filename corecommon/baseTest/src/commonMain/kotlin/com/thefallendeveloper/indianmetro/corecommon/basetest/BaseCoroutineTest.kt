package com.thefallendeveloper.indianmetro.corecommon.basetest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseCoroutineTest {
    protected val testDispatcher: TestDispatcher = StandardTestDispatcher()

    open fun setUpBaseCoroutineTest() {
        Dispatchers.setMain(testDispatcher)
    }

    open fun tearDownBaseCoroutineTest() {
        Dispatchers.resetMain()
    }
}
