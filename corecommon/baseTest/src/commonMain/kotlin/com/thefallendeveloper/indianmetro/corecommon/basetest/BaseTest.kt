package com.thefallendeveloper.indianmetro.corecommon.basetest

import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls

interface BaseTestSupport {
    fun registerMocks(vararg mockInstances: Any)
}

class BaseTest :
    BaseTestSupport,
    AfterTestSupport {
    private val mocks = mutableListOf<Any>()

    override fun registerMocks(vararg mockInstances: Any) {
        mocks.addAll(mockInstances.asList())
    }

    override fun afterTest() {
        if (mocks.isNotEmpty()) {
            mocks.forEach { mock ->
                resetCalls(mock)
                resetAnswers(mock)
            }
            mocks.clear()
        }
    }
}
