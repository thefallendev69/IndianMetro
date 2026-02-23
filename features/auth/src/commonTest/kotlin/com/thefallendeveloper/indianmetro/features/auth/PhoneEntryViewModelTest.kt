package com.thefallendeveloper.indianmetro.features.auth

import app.cash.turbine.test
import com.thefallendeveloper.indianmetro.baseunit.BaseTest
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigationRoutes
import com.thefallendeveloper.indianmetro.features.auth.navigation.OtpEntryArgs
import io.kotest.matchers.shouldBe

class PhoneEntryViewModelTest :
    BaseTest({
        test("initial state is empty and continue is disabled") {
            val viewModel = PhoneEntryViewModel(FeatureNavigator())

            viewModel.state.value.phoneNumber shouldBe ""
            viewModel.state.value.continueClickable shouldBe false
        }

        test("phone number changed keeps only ten digits") {
            val viewModel = PhoneEntryViewModel(FeatureNavigator())

            viewModel.emitEvent(PhoneEntryViewModel.Event.PhoneNumberChanged("12a34-5678909"))

            viewModel.state.value.phoneNumber shouldBe "1234567890"
            viewModel.state.value.continueClickable shouldBe true
        }

        test("continue clicked with invalid phone does not navigate") {
            val navigator = FeatureNavigator<AuthNavigationRoutes>()
            val viewModel = PhoneEntryViewModel(navigator)

            navigator.destination.test {
                viewModel.emitEvent(PhoneEntryViewModel.Event.ContinueClicked)
                expectNoEvents()
            }
        }

        test("continue clicked with valid phone navigates to otp entry") {
            val navigator = FeatureNavigator<AuthNavigationRoutes>()
            val viewModel = PhoneEntryViewModel(navigator)

            navigator.destination.test {
                viewModel.emitEvent(PhoneEntryViewModel.Event.PhoneNumberChanged("1234567890"))
                viewModel.emitEvent(PhoneEntryViewModel.Event.ContinueClicked)

                awaitItem() shouldBe AuthNavigationRoutes.OtpEntry(args = OtpEntryArgs(phoneNumber = "1234567890"))
            }
        }
    })
