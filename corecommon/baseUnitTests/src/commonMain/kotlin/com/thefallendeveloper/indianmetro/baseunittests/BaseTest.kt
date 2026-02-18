package com.thefallendeveloper.indianmetro.baseunittests

import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

abstract class BaseTest {
    protected val testDispatcher = StandardTestDispatcher()

    protected open fun doBeforeEachTest() = Unit

    protected open fun doAfterEachTest() = Unit

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun setUpBaseTest() {
        Dispatchers.setMain(testDispatcher)
        doBeforeEachTest()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun tearDownBaseTest() {
        doAfterEachTest()
        clearAllMocks()
        Dispatchers.resetMain()
    }
}
