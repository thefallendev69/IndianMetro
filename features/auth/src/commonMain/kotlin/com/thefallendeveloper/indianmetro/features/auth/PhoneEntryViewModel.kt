package com.thefallendeveloper.indianmetro.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PhoneEntryViewModel : ViewModel() {

    private val _events = MutableSharedFlow<Event>()

    val state: StateFlow<State> = events().scanState(initState()) { prevState, event ->
            when (event) {
                is Event.PhoneNumberChanged -> reduceStateOnPhoneNumberChanged(prevState, event)
                Event.ContinueClicked -> reduceStateOnContinueButtonClicked(prevState)
                is Event.OtpChanged -> reduceStateOnOtpChanged(prevState, event)
                Event.VerifyClicked -> reduceStateOnVerifyClicked(prevState)
                Event.ResendClicked -> reduceStateOnResendClicked(prevState)
            }
        }

    fun emitEvent(event: Event) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    private fun reduceStateOnPhoneNumberChanged(
        prevState: State,
        event: Event.PhoneNumberChanged,
    ): State {
        val phoneNumber = event.value.filter(Char::isDigit).take(MAX_PHONE_LENGTH)
        return prevState.copy(phoneNumber = phoneNumber)
    }

    private fun reduceStateOnContinueButtonClicked(prevState: State): State =
        if (prevState.continueClickable) {
            prevState.copy(authStep = AuthStep.OtpEntry)
        } else {
            prevState
        }

    private fun reduceStateOnOtpChanged(
        prevState: State,
        event: Event.OtpChanged,
    ): State {
        val otp = event.value.filter(Char::isDigit).take(MAX_OTP_LENGTH)
        return prevState.copy(otp = otp)
    }

    private fun reduceStateOnVerifyClicked(prevState: State): State = prevState

    private fun reduceStateOnResendClicked(prevState: State): State = prevState.copy(otp = "")

    data class State(
        val phoneNumber: String,
        val otp: String,
        val authStep: AuthStep,
    ) {
        val continueClickable: Boolean
            get() = phoneNumber.length == MAX_PHONE_LENGTH

        val verifyClickable: Boolean
            get() = otp.length == MAX_OTP_LENGTH
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

    sealed class Effect {
        data class NavigateToOnboarding(
            val phoneNumber: String,
        ) : Effect()
    }

    enum class AuthStep {
        PhoneEntry,
        OtpEntry,
    }

    private fun initState(): State =
        State(
            phoneNumber = "",
            otp = "",
            authStep = AuthStep.PhoneEntry,
        )

    private fun events() = _events

    private fun SharedFlow<Event>.scanState(
        initialState: State,
        reducer: (State, Event) -> State,
    ): StateFlow<State> =
        scan(initialState, reducer).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = initialState,
        )

    private companion object {
        const val MAX_PHONE_LENGTH = 10
        const val MAX_OTP_LENGTH = 6
    }
}
