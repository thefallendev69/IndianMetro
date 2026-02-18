package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.baseunittests.BaseTest
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigationRoutes
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PhoneEntryViewModelTest : BaseTest() {
    @MockK(relaxed = true)
    private lateinit var featureNavigator: FeatureNavigator<AuthNavigationRoutes>
    private lateinit var viewModel: PhoneEntryViewModel

    @BeforeTest
    fun setUp() {
        setUpBaseTest()
    }

    @AfterTest
    fun tearDown() {
        tearDownBaseTest()
    }

    override fun doBeforeEachTest() {
        MockKAnnotations.init(this)
        viewModel = createViewModel(featureNavigator)
    }

    @Test
    fun initialStateIsEmptyAndContinueIsNotClickable() {
        assertEquals("", viewModel.state.value.phoneNumber)
        assertFalse(viewModel.state.value.continueClickable)
    }

    @Test
    fun phoneNumberChangedFiltersNonDigitsAndCapsLengthToTen() =
        runTest {
            viewModel.emitEvent(PhoneEntryViewModel.Event.PhoneNumberChanged("12a34-5678909"))
            testScheduler.runCurrent()

            assertEquals("1234567890", viewModel.state.value.phoneNumber)
            assertTrue(viewModel.state.value.continueClickable)
        }

    @Test
    fun continueClickedWithInvalidPhoneNumberDoesNotNavigate() =
        runTest {
            viewModel.emitEvent(PhoneEntryViewModel.Event.ContinueClicked)
            testScheduler.runCurrent()

            verify(exactly = 0) { featureNavigator.navigateTo(any()) }
        }

    @Test
    fun continueClickedWithValidPhoneNumberNavigatesToOtpEntry() =
        runTest {
            viewModel.emitEvent(PhoneEntryViewModel.Event.PhoneNumberChanged("1234567890"))
            testScheduler.runCurrent()
            viewModel.emitEvent(PhoneEntryViewModel.Event.ContinueClicked)
            testScheduler.runCurrent()

            verify(exactly = 1) {
                featureNavigator.navigateTo(
                    match { route ->
                        route is AuthNavigationRoutes.OtpEntry && route.args.phoneNumber == "1234567890"
                    },
                )
            }
        }

    private fun createViewModel(featureNavigator: FeatureNavigator<AuthNavigationRoutes>): PhoneEntryViewModel =
        PhoneEntryViewModel(
            featureNavigator = featureNavigator,
        )
}
