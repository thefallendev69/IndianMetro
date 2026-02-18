package com.thefallendeveloper.indianmetro.baseunittests

import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class BaseTest {
    protected val testDispatcher = StandardTestDispatcher()

    protected open fun doBeforeEachTest() = Unit

    protected open fun doAfterEachTest() = Unit

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUpDispatcher() {
        Dispatchers.setMain(testDispatcher)
        doBeforeEachTest()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDownDispatcher() {
        doAfterEachTest()
        clearAllMocks()
        Dispatchers.resetMain()
    }
}
