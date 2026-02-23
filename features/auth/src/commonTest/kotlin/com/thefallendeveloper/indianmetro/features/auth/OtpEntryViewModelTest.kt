package com.thefallendeveloper.indianmetro.features.auth

import app.cash.turbine.test
import com.thefallendeveloper.indianmetro.baseunit.BaseTest
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.AppRoutes
import io.kotest.matchers.shouldBe

private const val TEST_PHONE_NUMBER = "9999999999"

class OtpEntryViewModelTest :
    BaseTest({
        test("initial state contains phone number and empty otp") {
            val viewModel =
                OtpEntryViewModel(
                    phoneNumber = TEST_PHONE_NUMBER,
                    appNavigator = FeatureNavigator(),
                )

            viewModel.state.value.phoneNumber shouldBe TEST_PHONE_NUMBER
            viewModel.state.value.otp shouldBe ""
            viewModel.state.value.verifyClickable shouldBe false
        }

        test("otp changed keeps only six digits") {
            val viewModel =
                OtpEntryViewModel(
                    phoneNumber = TEST_PHONE_NUMBER,
                    appNavigator = FeatureNavigator(),
                )

            viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged("12a345678"))

            viewModel.state.value.otp shouldBe "123456"
            viewModel.state.value.verifyClickable shouldBe true
        }

        test("resend clears otp") {
            val viewModel =
                OtpEntryViewModel(
                    phoneNumber = TEST_PHONE_NUMBER,
                    appNavigator = FeatureNavigator(),
                )

            viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged("123456"))
            viewModel.emitEvent(OtpEntryViewModel.Event.ResendClicked)

            viewModel.state.value.otp shouldBe ""
            viewModel.state.value.verifyClickable shouldBe false
        }

        test("verify clicked with invalid otp does not navigate") {
            val navigator = FeatureNavigator<AppRoutes>()
            val viewModel =
                OtpEntryViewModel(
                    phoneNumber = TEST_PHONE_NUMBER,
                    appNavigator = navigator,
                )

            navigator.destination.test {
                viewModel.emitEvent(OtpEntryViewModel.Event.VerifyClicked)
                expectNoEvents()
            }
        }

        test("verify clicked with valid otp navigates to onboarding") {
            val navigator = FeatureNavigator<AppRoutes>()
            val viewModel =
                OtpEntryViewModel(
                    phoneNumber = TEST_PHONE_NUMBER,
                    appNavigator = navigator,
                )

            navigator.destination.test {
                viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged("123456"))
                viewModel.emitEvent(OtpEntryViewModel.Event.VerifyClicked)

                awaitItem() shouldBe AppRoutes.AppOnboarding
            }
        }
    })
