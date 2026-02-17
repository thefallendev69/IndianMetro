package com.thefallendeveloper.indianmetro.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigationRoutes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PhoneEntryViewModel(
    private val featureNavigator: FeatureNavigator<AuthNavigationRoutes>,
) : ViewModel() {
    private val events = MutableSharedFlow<Event>()

    val state: StateFlow<State> =
        events
            .scan(initState()) { prevState, event ->
                when (event) {
                    is Event.PhoneNumberChanged -> reduceStateOnPhoneNumberChanged(prevState, event)
                    Event.ContinueClicked -> reduceStateOnContinueButtonClicked(prevState)
                    is Event.OtpChanged -> reduceStateOnOtpChanged(prevState, event)
                    Event.VerifyClicked -> reduceStateOnVerifyClicked(prevState)
                    Event.ResendClicked -> reduceStateOnResendClicked(prevState)
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = initState(),
            )

    fun emitEvent(event: Event) {
        viewModelScope.launch {
            events.emit(event)
            when (event) {
                Event.ContinueClicked -> {
                    if (state.value.continueClickable) {
                        featureNavigator.navigateTo(AuthNavigationRoutes.OtpEntry)
                    }
                }

                else -> Unit
            }
        }
    }

    sealed class Event {
        data class PhoneNumberChanged(
            val value: String,
        ) : Event()

        data object ContinueClicked : Event()

        data class OtpChanged(
            val value: String,
        ) : Event()

        data object VerifyClicked : Event()

        data object ResendClicked : Event()
    }

    data class State(
        val phoneNumber: String,
        val otp: String,
    ) {
        val continueClickable: Boolean
            get() = phoneNumber.length == MAX_PHONE_LENGTH

        val verifyClickable: Boolean
            get() = otp.length == MAX_OTP_LENGTH
    }

    private fun initState() =
        State(
            phoneNumber = "",
            otp = "",
        )

    private fun reduceStateOnPhoneNumberChanged(
        prevState: State,
        event: Event.PhoneNumberChanged,
    ): State {
        val value = event.value.filter(Char::isDigit).take(MAX_PHONE_LENGTH)
        return prevState.copy(phoneNumber = value)
    }

    private fun reduceStateOnContinueButtonClicked(prevState: State): State = prevState

    private fun reduceStateOnOtpChanged(
        prevState: State,
        event: Event.OtpChanged,
    ): State {
        val value = event.value.filter(Char::isDigit).take(MAX_OTP_LENGTH)
        return prevState.copy(otp = value)
    }

    private fun reduceStateOnVerifyClicked(prevState: State): State = prevState

    private fun reduceStateOnResendClicked(prevState: State): State = prevState.copy(otp = "")

    private companion object {
        const val MAX_PHONE_LENGTH = 10
        const val MAX_OTP_LENGTH = 6
    }
}
