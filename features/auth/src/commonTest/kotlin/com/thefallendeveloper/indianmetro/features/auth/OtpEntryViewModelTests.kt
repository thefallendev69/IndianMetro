package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.corecommon.basetest.BaseTest
import com.thefallendeveloper.indianmetro.corecommon.basetest.BaseTestSupport
import com.thefallendeveloper.indianmetro.corecommon.basetest.CoroutineSupport
import com.thefallendeveloper.indianmetro.corecommon.basetest.CoroutineTest
import com.thefallendeveloper.indianmetro.corecommon.basetest.KoinSupport
import com.thefallendeveloper.indianmetro.corecommon.basetest.KoinTestSupport
import com.thefallendeveloper.indianmetro.corecommon.basetest.ManagedTestLifecycleHooks
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.AppNavigator
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.AppRoutes
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private val otpEntryViewModelTestModule =
    module {
        single(named<AppNavigator>()) { FeatureNavigator<AppRoutes>() }
        factory { (phoneNumber: String) ->
            OtpEntryViewModel(
                phoneNumber = phoneNumber,
                appNavigator = get(named<AppNavigator>()),
            )
        }
    }

@OptIn(ExperimentalCoroutinesApi::class)
class OtpEntryViewModelTests :
    KoinTest,
    ManagedTestLifecycleHooks,
    CoroutineSupport by CoroutineTest(),
    KoinSupport by KoinTestSupport(),
    BaseTestSupport by BaseTest() {
    private lateinit var appNavigator: FeatureNavigator<AppRoutes>
    private lateinit var viewModel: OtpEntryViewModel

    @BeforeTest
    fun setUp() {
        setUpManagedTestLifecycle()
        startKoinForTest(otpEntryViewModelTestModule)
        appNavigator = get(qualifier = named<AppNavigator>())
        viewModel = get { parametersOf(TEST_PHONE_NUMBER) }
    }

    @AfterTest
    fun tearDown() {
        stopKoinForTest()
        tearDownManagedTestLifecycle()
    }

    @Test
    fun initialStateContainsPhoneAndEmptyOtp() =
        runTest {
            assertEquals(TEST_PHONE_NUMBER, viewModel.state.value.phoneNumber)
            assertEquals("", viewModel.state.value.otp)
            assertFalse(viewModel.state.value.verifyClickable)
        }

    @Test
    fun otpChangedFiltersNonDigitsAndLimitsLengthToSix() =
        runTest {
            viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged("12a345678"))
            val updatedState = viewModel.state.first { it.otp == "123456" }
            assertEquals("123456", updatedState.otp)
            assertTrue(updatedState.verifyClickable)
        }

    @Test
    fun verifyClickedWithInvalidOtpDoesNotNavigate() =
        runTest {
            val emittedRouteDeferred =
                async(start = CoroutineStart.UNDISPATCHED) { appNavigator.destination.first() }
            viewModel.emitEvent(OtpEntryViewModel.Event.VerifyClicked)
            assertFalse(emittedRouteDeferred.isCompleted)
            emittedRouteDeferred.cancel()
        }

    @Test
    fun verifyClickedWithValidOtpNavigatesToOnboarding() =
        runTest {
            val emittedRouteDeferred =
                async(start = CoroutineStart.UNDISPATCHED) { appNavigator.destination.first() }
            viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged("123456"))
            viewModel.state.first { it.verifyClickable }
            viewModel.emitEvent(OtpEntryViewModel.Event.VerifyClicked)
            assertEquals(AppRoutes.AppOnboarding, emittedRouteDeferred.await())
        }

    @Test
    fun resendClickedClearsOtp() =
        runTest {
            viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged("123456"))
            viewModel.state.first { it.otp == "123456" }
            viewModel.emitEvent(OtpEntryViewModel.Event.ResendClicked)
            val resetState = viewModel.state.first { it.otp == "" }
            assertEquals("", resetState.otp)
            assertFalse(resetState.verifyClickable)
        }

    private companion object {
        const val TEST_PHONE_NUMBER = "9999999999"
    }
}
