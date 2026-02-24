package com.thefallendeveloper.indianmetro.corecommon.basetest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

interface CoroutineSupport : BeforeTestSupport, AfterTestSupport

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTest(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : CoroutineSupport {
    override fun beforeTest() {
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterTest() {
        Dispatchers.resetMain()
    }
}
