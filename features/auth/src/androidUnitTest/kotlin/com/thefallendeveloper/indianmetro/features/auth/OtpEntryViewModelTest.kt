package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.baseunittests.BaseTest
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.AppRoutes
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class OtpEntryViewModelTest : BaseTest() {
    private lateinit var appNavigator: FeatureNavigator<AppRoutes>
    private lateinit var viewModel: OtpEntryViewModel

    override fun doBeforeEachTest() {
        appNavigator = mockk(relaxed = true)
        viewModel = createViewModel(appNavigator)
    }

    @Test
    fun initialStateSetsPhoneNumberAndKeepsOtpEmpty() {
        assertEquals(TEST_PHONE_NUMBER, viewModel.state.value.phoneNumber)
        assertEquals("", viewModel.state.value.otp)
        assertFalse(viewModel.state.value.verifyClickable)
    }

    @Test
    fun otpChangedFiltersNonDigitsAndCapsLengthToSix() =
        runTest {
            viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged("12a345678"))
            testScheduler.runCurrent()

            assertEquals("123456", viewModel.state.value.otp)
            assertTrue(viewModel.state.value.verifyClickable)
        }

    @Test
    fun resendClickedClearsOtpValue() =
        runTest {
            viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged("123456"))
            testScheduler.runCurrent()
            viewModel.emitEvent(OtpEntryViewModel.Event.ResendClicked)
            testScheduler.runCurrent()

            assertEquals("", viewModel.state.value.otp)
            assertFalse(viewModel.state.value.verifyClickable)
        }

    @Test
    fun verifyClickedWithInvalidOtpDoesNotNavigate() =
        runTest {
            viewModel.emitEvent(OtpEntryViewModel.Event.VerifyClicked)
            testScheduler.runCurrent()

            verify(exactly = 0) { appNavigator.navigateTo(any()) }
        }

    @Test
    fun verifyClickedWithValidOtpNavigatesToOnboarding() =
        runTest {
            viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged("123456"))
            testScheduler.runCurrent()
            viewModel.emitEvent(OtpEntryViewModel.Event.VerifyClicked)
            testScheduler.runCurrent()

            verify(exactly = 1) { appNavigator.navigateTo(AppRoutes.AppOnboarding) }
        }

    private fun createViewModel(appNavigator: FeatureNavigator<AppRoutes>): OtpEntryViewModel =
        OtpEntryViewModel(
            phoneNumber = TEST_PHONE_NUMBER,
            appNavigator = appNavigator,
            coroutineDispatchersProvider = TestCoroutineDispatchersProvider(testDispatcher),
        )

    private companion object {
        const val TEST_PHONE_NUMBER = "9999999999"
    }
}
