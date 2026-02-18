package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.baseunittests.BaseTest
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.AppRoutes
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

class OtpEntryViewModelTest : BaseTest() {
    @MockK(relaxed = true)
    private lateinit var appNavigator: FeatureNavigator<AppRoutes>
    private lateinit var viewModel: OtpEntryViewModel

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
        )

    private companion object {
        const val TEST_PHONE_NUMBER = "9999999999"
    }
}
