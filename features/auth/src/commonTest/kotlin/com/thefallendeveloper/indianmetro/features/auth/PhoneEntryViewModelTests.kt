package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.corecommon.basetest.BaseTest
import com.thefallendeveloper.indianmetro.corecommon.basetest.BaseTestSupport
import com.thefallendeveloper.indianmetro.corecommon.basetest.CoroutineSupport
import com.thefallendeveloper.indianmetro.corecommon.basetest.CoroutineTest
import com.thefallendeveloper.indianmetro.corecommon.basetest.KoinSupport
import com.thefallendeveloper.indianmetro.corecommon.basetest.KoinTestSupport
import com.thefallendeveloper.indianmetro.corecommon.basetest.ManagedTestLifecycleHooks
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigationRoutes
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigator
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
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

private val phoneEntryViewModelTestModule =
    module {
        single(named<AuthNavigator>()) { FeatureNavigator<AuthNavigationRoutes>() }
        factory { PhoneEntryViewModel(featureNavigator = get(named<AuthNavigator>())) }
    }

@OptIn(ExperimentalCoroutinesApi::class)
class PhoneEntryViewModelTests :
    KoinTest,
    ManagedTestLifecycleHooks,
    CoroutineSupport by CoroutineTest(),
    KoinSupport by KoinTestSupport(),
    BaseTestSupport by BaseTest() {
    private lateinit var featureNavigator: FeatureNavigator<AuthNavigationRoutes>
    private lateinit var viewModel: PhoneEntryViewModel

    @BeforeTest
    fun setUp() {
        startKoinForTest(phoneEntryViewModelTestModule)
        featureNavigator = get(qualifier = named<AuthNavigator>())
        viewModel = get()
    }

    @AfterTest
    fun tearDown() {
        stopKoinForTest()
    }

    @Test
    fun initialStateIsEmptyAndContinueIsNotClickable() =
        runTest {
            assertEquals("", viewModel.state.value.phoneNumber)
            assertFalse(viewModel.state.value.continueClickable)
        }

    @Test
    fun phoneNumberChangedFiltersNonDigitsAndLimitsLengthToTen() =
        runTest {
            viewModel.emitEvent(PhoneEntryViewModel.Event.PhoneNumberChanged("12a345678901"))
            val updatedState = viewModel.state.first { it.phoneNumber == "1234567890" }

            assertEquals("1234567890", updatedState.phoneNumber)
            assertTrue(updatedState.continueClickable)
        }

    @Test
    fun continueClickedWithInvalidPhoneDoesNotNavigate() =
        runTest {
            val emittedRouteDeferred =
                async(start = CoroutineStart.UNDISPATCHED) { featureNavigator.destination.first() }

            viewModel.emitEvent(PhoneEntryViewModel.Event.ContinueClicked)

            assertFalse(emittedRouteDeferred.isCompleted)
            emittedRouteDeferred.cancel()
        }

    @Test
    fun continueClickedWithValidPhoneNavigatesToOtpEntry() =
        runTest {
            val emittedRouteDeferred =
                async(start = CoroutineStart.UNDISPATCHED) { featureNavigator.destination.first() }

            viewModel.emitEvent(PhoneEntryViewModel.Event.PhoneNumberChanged(TEST_PHONE_NUMBER))
            viewModel.state.first { it.continueClickable }
            viewModel.emitEvent(PhoneEntryViewModel.Event.ContinueClicked)

            val emittedRoute = emittedRouteDeferred.await()
            assertTrue(emittedRoute is AuthNavigationRoutes.OtpEntry)
            assertEquals(TEST_PHONE_NUMBER, emittedRoute.args.phoneNumber)
        }

    private companion object {
        const val TEST_PHONE_NUMBER = "9999999999"
    }
}
