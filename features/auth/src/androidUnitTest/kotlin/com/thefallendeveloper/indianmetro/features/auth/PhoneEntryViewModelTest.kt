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
    @Test
    fun initialState_isEmptyAndContinueIsNotClickable() {
        val featureNavigator = mockk<FeatureNavigator<AuthNavigationRoutes>>(relaxed = true)
        val viewModel = createViewModel(featureNavigator)

        assertEquals("", viewModel.state.value.phoneNumber)
        assertFalse(viewModel.state.value.continueClickable)
    }

    @Test
    fun phoneNumberChanged_filtersNonDigitsAndCapsLengthToTen() =
        runTest {
            val featureNavigator = mockk<FeatureNavigator<AuthNavigationRoutes>>(relaxed = true)
            val viewModel = createViewModel(featureNavigator)

            viewModel.emitEvent(PhoneEntryViewModel.Event.PhoneNumberChanged("12a34-5678909"))
            testScheduler.runCurrent()

            assertEquals("1234567890", viewModel.state.value.phoneNumber)
            assertTrue(viewModel.state.value.continueClickable)
        }

    @Test
    fun continueClicked_withInvalidPhoneNumber_doesNotNavigate() =
        runTest {
            val featureNavigator = mockk<FeatureNavigator<AuthNavigationRoutes>>(relaxed = true)
            val viewModel = createViewModel(featureNavigator)

            viewModel.emitEvent(PhoneEntryViewModel.Event.ContinueClicked)
            testScheduler.runCurrent()

            verify(exactly = 0) { featureNavigator.navigateTo(any()) }
        }

    @Test
    fun continueClicked_withValidPhoneNumber_navigatesToOtpEntry() =
        runTest {
            val featureNavigator = mockk<FeatureNavigator<AuthNavigationRoutes>>(relaxed = true)
            val viewModel = createViewModel(featureNavigator)

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
