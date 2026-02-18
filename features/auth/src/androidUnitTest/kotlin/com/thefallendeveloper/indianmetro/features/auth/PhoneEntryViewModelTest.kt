package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.baseunittests.BaseTest
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigationRoutes
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PhoneEntryViewModelTest : BaseTest() {
    private lateinit var featureNavigator: FeatureNavigator<AuthNavigationRoutes>
    private lateinit var viewModel: PhoneEntryViewModel

    override fun doBeforeEachTest() {
        featureNavigator = mockk(relaxed = true)
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
            coroutineDispatchersProvider = TestCoroutineDispatchersProvider(testDispatcher),
        )
}
