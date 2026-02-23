package com.thefallendeveloper.indianmetro.baseunit

import io.kotest.core.spec.style.FunSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseTest(
    body: BaseTest.() -> Unit = {},
) : FunSpec() {
    private val testDispatcher = UnconfinedTestDispatcher()

    init {
        beforeTest {
            Dispatchers.setMain(testDispatcher)
        }
        afterTest {
            Dispatchers.resetMain()
        }
        body()
    }
}
