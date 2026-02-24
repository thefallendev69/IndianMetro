package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.AppRoutes
import com.thefallendeveloper.indianmetro.corecommon.basetest.BaseTest
import com.thefallendeveloper.indianmetro.corecommon.basetest.BaseTestSupport
import com.thefallendeveloper.indianmetro.corecommon.basetest.CoroutineTest
import com.thefallendeveloper.indianmetro.corecommon.basetest.CoroutineSupport
import com.thefallendeveloper.indianmetro.corecommon.basetest.ManagedTestLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class OtpEntryViewModelTests :
    ManagedTestLifecycle,
    CoroutineSupport by CoroutineTest(),
    BaseTestSupport by BaseTest() {
    private lateinit var appNavigator: FeatureNavigator<AppRoutes>

    @BeforeTest
    fun setUp() {
        beforeManagedTestLifecycle()
        appNavigator = FeatureNavigator()
    }

    @AfterTest
    fun tearDown() {
        afterManagedTestLifecycle()
    }

    @Test
    fun initialStateContainsPhoneAndEmptyOtp() =
        runTest {
            val viewModel =
                OtpEntryViewModel(phoneNumber = TEST_PHONE_NUMBER, appNavigator = appNavigator)

            assertEquals(TEST_PHONE_NUMBER, viewModel.state.value.phoneNumber)
            assertEquals("", viewModel.state.value.otp)
            assertFalse(viewModel.state.value.verifyClickable)
        }

    @Test
    fun otpChangedFiltersNonDigitsAndLimitsLengthToSix() =
        runTest {
            val viewModel =
                OtpEntryViewModel(phoneNumber = TEST_PHONE_NUMBER, appNavigator = appNavigator)

            viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged("12a345678"))

            assertEquals("123456", viewModel.state.value.otp)
            assertTrue(viewModel.state.value.verifyClickable)
        }

    @Test
    fun verifyClickedWithInvalidOtpDoesNotNavigate() =
        runTest {
            val viewModel =
                OtpEntryViewModel(phoneNumber = TEST_PHONE_NUMBER, appNavigator = appNavigator)
            val emittedRoutes = mutableListOf<AppRoutes>()
            val collectJob =
                launch {
                    appNavigator.destination.collect { route ->
                        emittedRoutes += route
                    }
                }

            viewModel.emitEvent(OtpEntryViewModel.Event.VerifyClicked)

            assertEquals(emptyList(), emittedRoutes)
            collectJob.cancel()
        }

    @Test
    fun verifyClickedWithValidOtpNavigatesToOnboarding() =
        runTest {
            val viewModel = OtpEntryViewModel(phoneNumber = TEST_PHONE_NUMBER, appNavigator = appNavigator)
            val emittedRoute= appNavigator.destination.first()

            viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged("123456"))
            viewModel.emitEvent(OtpEntryViewModel.Event.VerifyClicked)

            assertEquals(AppRoutes.AppOnboarding, emittedRoute)
        }

    @Test
    fun resendClickedClearsOtp() =
        runTest {
            val viewModel =
                OtpEntryViewModel(phoneNumber = TEST_PHONE_NUMBER, appNavigator = appNavigator)

            viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged("123456"))
            viewModel.emitEvent(OtpEntryViewModel.Event.ResendClicked)

            assertEquals("", viewModel.state.value.otp)
            assertFalse(viewModel.state.value.verifyClickable)
        }

    private companion object {
        const val TEST_PHONE_NUMBER = "9999999999"
    }
}
